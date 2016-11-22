<?php
	include './BeTrackDataBase.php';

    //fetch table rows from mysql db
    $sql = "select * from BetrackConf";
    $result = mysqli_query($con, $sql) or die("Error in Selecting " . mysqli_error($con));

    //create an array
    $emparray = array();
    while($row =mysqli_fetch_assoc($result))
    {
        $emparray[] = $row;
    }
    echo json_encode($emparray);

    //close the db con
    mysqli_close($con);
?>
