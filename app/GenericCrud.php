<?php
include_once "Conexion.php";

class GenericCRUD
{
    private $pdo;

    public function __construct()
    {
        $this->pdo = (new Conexion())->getPdo();
    }

    public function getAll($table)
    {
        $stmt = $this->pdo->query("SELECT * FROM $table");
        echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
    }

    public function getById($table, $idField, $idValue)
    {
        $stmt = $this->pdo->prepare("SELECT * FROM $table WHERE $idField = ?");
        $stmt->execute([$idValue]);
        echo json_encode($stmt->fetch(PDO::FETCH_ASSOC) ?: ["success" => false]);
    }

    public function insert($table, $data)
    {
        if (empty($data)) {
            echo json_encode(["success" => false, "error" => "No hay datos para insertar"]);
            return;
        }

        $columns = implode(",", array_keys($data));
        $placeholders = implode(",", array_fill(0, count($data), "?"));
        $values = array_values($data);

        $sql = "INSERT INTO $table ($columns) VALUES ($placeholders)";
        $stmt = $this->pdo->prepare($sql);

        if ($stmt->execute($values)) {
            echo json_encode(["success" => true]);
        } else {
            echo json_encode(["success" => false, "error" => $stmt->errorInfo()]);
        }
    }


    public function update($table, $idField, $idValue, $data)
    {
        $set = implode(", ", array_map(fn($key) => "$key = ?", array_keys($data)));
        $values = array_values($data);
        $values[] = $idValue;

        $sql = "UPDATE $table SET $set WHERE $idField = ?";
        $this->executeTransaction($sql, $values);
    }

    public function delete($table, $idField, $idValue)
    {
        $sql = "DELETE FROM $table WHERE $idField = ?";
        $this->executeTransaction($sql, [$idValue]);
    }

    // Relación M:N - Insertar
    public function insertRelation($pivotTable, $key1, $key2, $id1, $id2)
    {
        $sql = "INSERT INTO $pivotTable ($key1, $key2) VALUES (?, ?)";
        $this->executeTransaction($sql, [$id1, $id2]);
    }

    // Relación M:N - Borrar
    public function deleteRelation($pivotTable, $key1, $key2, $id1, $id2)
    {
        $sql = "DELETE FROM $pivotTable WHERE $key1 = ? AND $key2 = ?";
        $this->executeTransaction($sql, [$id1, $id2]);
    }

    // Relación M:N - Obtener relacionados
    public function getRelated($pivotTable, $key, $value)
    {
        $stmt = $this->pdo->prepare("SELECT * FROM $pivotTable WHERE $key = ?");
        $stmt->execute([$value]);
        echo json_encode($stmt->fetchAll(PDO::FETCH_ASSOC));
    }

    private function executeTransaction($sql, $params)
    {
        try {
            $this->pdo->beginTransaction();
            $stmt = $this->pdo->prepare($sql);
            $stmt->execute($params);
            $this->pdo->commit();
            echo json_encode(["success" => true]);
        } catch (Exception $e) {
            $this->pdo->rollBack();
            echo json_encode(["success" => false]);
        }
    }
}
