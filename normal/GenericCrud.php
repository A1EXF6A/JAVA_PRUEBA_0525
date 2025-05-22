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
        $columns = implode(",", array_keys($data));
        $placeholders = implode(",", array_fill(0, count($data), "?"));
        $values = array_values($data);

        $sql = "INSERT INTO $table ($columns) VALUES ($placeholders)";
        $this->executeTransaction($sql, $values);
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
