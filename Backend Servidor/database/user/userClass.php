<?php
	include('../dbFunctions.php');

	class User {
		/**
		 *	Return a array with the query result. The query only can be execute by searching by email.
		 *	@param email
		 */
		function select($email, $password) {
			$connection = connectDB();

			$sql = mysqli_prepare($connection, "SELECT * FROM usuario WHERE email = ?");
			mysqli_stmt_bind_param($sql, "s", $email);

			$query = $sql->execute();

			if(!$query)
            	        die();
			$result = $sql->store_result();

			$realresult = $sql->bind_result($id, $email, $name, $passHash);

			$rawdata = array();

			$sql->fetch();

			$correct = password_verify($password, $passHash);

			$rawdata['id'] = utf8_encode($id);
			$rawdata['email'] = utf8_encode($email);
			$rawdata['name'] = utf8_encode($name);
			$rawdata['correct'] = $correct;

	                disconnectDB($connection);
		        return $rawdata;
		}

		function getId($email) {
                        $connection = connectDB();

                        $sql = mysqli_prepare($connection, "SELECT id FROM usuario WHERE email = ?");
                        mysqli_stmt_bind_param($sql, "s", $email);

                        $query = $sql->execute();

                        if(!$query)
                        die();

                        $result = $sql->store_result();

                        $realresult = $sql->bind_result($id);

                        $rawdata = array();

			$correct = $query;

                        $sql->fetch();

                        $rawdata['id'] = utf8_encode($id);
			$rawdata['correct'] = $correct;

                        disconnectDB($connection);
                        return $rawdata;
                }



		/**
		 *	Insert a user in the database.
		 *	@param email
		 *	@param name
		 *	@param password
		 */
		function insert($email, $name, $password) {
	        $connection = connectDB();

		$passHash = password_hash($password, PASSWORD_BCRYPT);

	        $sql = mysqli_prepare($connection, "INSERT INTO usuario (id, email, nombre, contrasena) VALUES (null, ?, ?, ?)");

	        mysqli_stmt_bind_param($sql, "sss", $email, $name, $passHash);

	        $query = $sql->execute();

	        if(!$query)
	            echo "incorrect";
	        else
	            echo "correct";

	        disconnectDB($connection);
	        return $query;
	    }
	}
?>