#! /bin/bash

tmpd=`mktemp -d`
tmpf="$tmpd"/fifoout
mkfifo "$tmpf"
printf "%s\n" "$tmpf"   # printing the file path to fifo might be useful
>back.txt

arr=(32)
end=$((SECONDS+1500))   # timed loop because end condition unclear

while [ $SECONDS -lt $end ]; do
	var=0
	lost=false		
 
 	nc numb3rs.thenixuchallenge.com 1337 < "$tmpf" > /home/kali/Scripts/back.txt &
	ncpid=$!  # PID for later

	exec 3> "$tmpf"

	sleep 1   # sleep is used to avoid disconnection
	echo ${arr[$var]} >&3

	while [ $lost = false ]; do			
		if [ $((${#arr[@]} - 1 )) -gt $var ]; then 	
			sleep .3
			let "var++"
			echo ${arr[$var]} >&3
			
		else
			sleep .3
			echo ${arr[$var]} >&3
			
			sleep 1.5   # sleep here gives the server enough time to relay data
			newnumber=`tail -n 2 /home/kali/Scripts/back.txt | head -n 1`
			arr+=($newnumber)	
			lost=true			 
		fi
		done 	
	
	kill $ncpid
	exec 3>&-
	sleep 0.5
	
done

rm -r "$tmpd"
