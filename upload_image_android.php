<?php
$target_path="./your_directory/";
$target_path=$target_path.basename($_FILES['uploadImage']['name']);
move_uploaded_file($_FILES['uploadImage']['tmp_name'],$target_path);
?>