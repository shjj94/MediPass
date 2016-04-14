<?php
   error_reporting(E_ALL);//모든 에러를 기록한다
   ini_set("display_errors", 1);//에러 메세지를 화면에 노출. 1:활성화/0:비활성화

   header('Content-Type: charset=utf-8');
   include_once("../dbConnect-medipass.php");

   $count = ($_POST['count']);
   $user_id = ($_POST['user_id']);
   $prescription_num = ($_POST['prescription_num']);
   $valid_date = ($_POST['valid_date']);
   $pharm_code = ($_POST['pharm_code']);
   $is_used = 0;

   for($i=0;$i<$count;$i++){
   	$medicine_name = ($_POST['medName']);
   	$medicine_duration = ($_POST['numDay1']);
   	$medicine_once_num = ($_POST['amount1']);
   	$medicine_day_num = ($_POST['num1']);
   	$notice = ($_POST['note1']);

   	$sql = "insert into MEDICAL_RECORD(num, user_id, prescription_num, valid_date, is_used, medicine_name, medicine_duration, medicine_once_num, medicine_day_num, notice, pharm_code) values('', '$user_id', '$prescription_num', '$valid_date', '$is_used', '$medicine_name[$i]', '$medicine_duration', '$medicine_once_num', '$medicine_day_num', '$notice', '$pharm_code')";

   	$result = mysqli_query($conn, $sql) or die(mysqli_error($conn));
   }

   $mysqli_close($conn);

   echo "submit OK";

?>