#! /bin/bash

tmpd=`mktemp -d`
tmpf="$tmpd"/fifoout
mkfifo "$tmpf"   # fifo pipe for netcat
printf "%s\n" "$tmpf"   # printing the file path to fifo might be useful
>back.txt   # text file to read and save the server output

# deleting the tmp files and opening the output file
function cleanup {
	rm -r "$tmpd"
	xdg-open back.txt
}

trap cleanup EXIT

arr=(32)
end=$((SECONDS+1500))   # timed loop because end condition unclear

while [ $SECONDS -lt $end ]; do
	var=0		
 
 	nc numb3rs.thenixuchallenge.com 1337 < "$tmpf" > "back.txt" &
	ncpid=$!  # PID for later
	exec 3> "$tmpf"

	sleep 1.1   # sleep is used to avoid disconnection
	echo ${arr[$var]} >&3

	while [ $((${#arr[@]} - 1 )) -gt $var ]; do		
		sleep .3
		let "var++"
		echo ${arr[$var]} >&3	
	done
	
	sleep .3
	echo ${arr[$var]} >&3   # sending a wrong number to get the right one for next iteration
			
	sleep 1.5   # sleep here needed to give the server enough time to relay data
	newnumber=`tail -n 2 "back.txt" | head -n 1`
	arr+=($newnumber)				 
		
	kill $ncpid
	exec 3>&-
	sleep 0.5	
done
