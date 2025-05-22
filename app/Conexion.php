<?php
class Conexion
{
    private $pdo;

    public function __construct()
    {
        $option = array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8');
        try {
            $host = getenv('MYSQL_HOST');       // o $_SERVER['MYSQL_HOST']
            $db = getenv('MYSQL_DATABASE');
            $user = getenv('MYSQL_USER');
            $pass = getenv('MYSQL_PASSWORD');

            $this->pdo = new PDO("mysql:host=$host;dbname=$db", $user, $pass, $option);
            $this->pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (Exception $e) {
            die("Error en la conexión: " . $e->getMessage());
        }
    }

    public function getPdo()
    {
        return $this->pdo;
    }
}

?>