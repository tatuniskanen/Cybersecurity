
#! /bin/bash

arr=()
mapfile -t arr < /home/kali/scripts/vars.txt   # using variables from the last iteration in case the script times out
end=$((SECONDS+900))   # timed loop because end condition unclear

while [ $SECONDS -lt $end ]; do
	var=0
	lost=false		
	sent=false
 
 	nc numb3rs.thenixuchallenge.com 1337 < /tmp/tmp.qhS32Q4cEL/fifoout > /home/kali/scripts/back.txt &
	ncpid=$!  # PID for later

	exec 3> /tmp/tmp.qhS32Q4cEL/fifoout

	sleep 1   # sleep is used to avoid disconnection
	echo ${arr[$var]} >&3

	while [ $lost = false ]; do			
		if [ $((${#arr[@]} - 1 )) -gt $var ]; then 	
			sleep .3
			let "var++"
			echo ${arr[$var]} >&3
		
		elif [ $sent = false ]; then
			sleep .3
			echo ${arr[$var]} >&3
			sent=true
		
		else
			sleep 1.5
			newnumber=`tail -n 2 /home/kali/scripts/back.txt | head -n 1`
			arr+=($newnumber)	
			lost=true			 
		fi
		done 	
	
	kill $ncpid
	exec 3>&-
	sleep 0.5
	
done

printf "%s\n" "${arr[@]}" > /home/kali/scripts/vars.txt
