<?php
include_once "GenericCRUD.php";
header('Content-Type: application/json');

$method = $_SERVER['REQUEST_METHOD'];
$crud = new GenericCRUD();

// Manejo de datos según método
switch ($method) {
    case "GET":
        $table = $_GET["table"] ?? null;
        $idField = $_GET["idField"] ?? null;
        $idValue = $_GET["idValue"] ?? null;
        $relation = $_GET["relation"] ?? false;

        if ($relation) {
            $pivot = $_GET["pivotTable"] ?? null;
            $fk = $_GET["foreignKey"] ?? null;
            $id = $_GET["id"] ?? null;

            if ($pivot && $fk && $id) {
                $crud->getRelated($pivot, $fk, $id);
            } else {
                echo json_encode(["success" => false, "error" => "Faltan parámetros para la relación"]);
            }
            exit;
        }

        if (!$table) {
            echo json_encode(["success" => false, "error" => "Parámetro 'table' requerido"]);
            exit;
        }

        if ($idField && $idValue) {
            $crud->getById($table, $idField, $idValue);
        } else {
            $crud->getAll($table);
        }
        break;

    case "POST":
    case "PUT":
    case "DELETE":
        $data = json_decode(file_get_contents("php://input"), true);

        $table = $data["table"] ?? null;
        $idField = $data["idField"] ?? null;
        $idValue = $data["idValue"] ?? null;
        $payload = $data["data"] ?? [];
        $relation = $data["relation"] ?? false;

        if ($relation) {
            $pivot = $data["pivotTable"];
            $fk1 = $data["foreignKey1"];
            $fk2 = $data["foreignKey2"];
            $id1 = $data["id1"];
            $id2 = $data["id2"];

            switch ($method) {
                case "POST":
                    $crud->insertRelation($pivot, $fk1, $fk2, $id1, $id2);
                    break;
                case "DELETE":
                    $crud->deleteRelation($pivot, $fk1, $fk2, $id1, $id2);
                    break;
                default:
                    echo json_encode(["success" => false, "error" => "Método no permitido para relación"]);
                    break;
            }
            exit;
        }

        if (!$table) {
            echo json_encode(["success" => false, "error" => "Parámetro 'table' requerido"]);
            exit;
        }

        switch ($method) {
            case "POST":
                $crud->insert($table, $payload);
                break;

            case "PUT":
                if (!$idValue) {
                    echo json_encode(["success" => false, "error" => "ID requerido para actualizar"]);
                    break;
                }
                unset($payload[$idField]);
                $crud->update($table, $idField, $idValue, $payload);
                break;

            case "DELETE":
                if (!$idValue) {
                    echo json_encode(["success" => false, "error" => "ID requerido para eliminar"]);
                    break;
                }
                $crud->delete($table, $idField, $idValue);
                break;
        }
        break;

    default:
        echo json_encode(["success" => false, "error" => "Método no permitido"]);
        break;
}
