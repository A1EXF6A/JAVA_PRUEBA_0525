<?php
include_once "GenericCRUD.php";

header('Content-Type: application/json');

$method = $_SERVER['REQUEST_METHOD'];
$crud = new GenericCRUD();

// Tabla y campo clave definidos directamente
$table = "estudiantes";
$idField = "cedula";

switch ($method) {
    case 'GET':
        $idValue = $_GET[$idField] ?? null;
        if ($idValue) {
            $crud->getById($table, $idField, $idValue);
        } else {
            $crud->getAll($table);
        }
        break;

    case 'POST':
        $data = json_decode(file_get_contents("php://input"), true) ?? $_POST;
        $crud->insert($table, $data);
        break;

    case 'PUT':
        $data = json_decode(file_get_contents("php://input"), true);
        $idValue = $data[$idField] ?? null;
        if (!$idValue) {
            echo json_encode(["success" => false]);
            break;
        }
        unset($data[$idField]);
        $crud->update($table, $idField, $idValue, $data);
        break;

    case 'DELETE':
        $data = json_decode(file_get_contents("php://input"), true);
        $idValue = $data[$idField] ?? null;
        if (!$idValue) {
            echo json_encode(["success" => false, "error" => "Identificador requerido para eliminar"]);
            break;
        }
        $crud->delete($table, $idField, $idValue);
        break;


    default:
        echo json_encode(["success" => false, "error" => "Método no permitido"]);
        break;
}
