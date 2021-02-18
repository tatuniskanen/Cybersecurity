# numb3rs

This was a simple scripting task to establish a two-way communication with a server using netcat. Made with Bash.

The goal was to find the right combination of numbers to send and get the flag. The numbers need to be sent to the server one by one in the right order and within reasonable time (not too slow and not too fast). Upon failure the server tells you the right number you missed. This is ideally done with a single script.

My solution involved looping the numbers with netcat, from a text file, through a fifo pipe, and adding new numbers to the text file iteratively.
There are other ways to do this, but this is the solution I came up with.
