<?php
class Conexion
{
    private $pdo;

    public function __construct()
    {
        $options = array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8');

        try {
            $host = 'localhost';
            $port = 3308;
            $db = 'productos';
            $user = 'root';
            $pass = '';

            $dsn = "mysql:host=$host;port=$port;dbname=$db";

            $this->pdo = new PDO($dsn, $user, $pass, $options);
            $this->pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (PDOException $e) {
            die("Error en la conexión: " . $e->getMessage());
        }
    }

    public function getPdo()
    {
        return $this->pdo;
    }
}
?>