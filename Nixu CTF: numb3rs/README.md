# numb3rs

## Assignment
https://thenixuchallenge.com/c/numb3rs/

"There is only one combination of numbers that gives you the answer." 

"nc numb3rs.thenixuchallenge.com 1337"

## Execution

This was a simple scripting task to establish a two-way communication with a server using netcat.

The goal was to find the right combination of numbers to send and get the flag. The numbers need to be sent to the server one by one in the right order and within reasonable time (not too slow and not too fast, to avoid disconnection). Upon failure the server tells you the right number you missed. This is ideally done within a single script.

My solution involved using a loop to send the numbers to the server with netcat, from a text file, through a fifo pipe, and adding new numbers to the text file iteratively.
There are other ways to do this, but this is the solution I came up with.
