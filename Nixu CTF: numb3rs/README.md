This was a simple scripting task to communicate with a server using netcat. Made with Bash.
The goal was to find the right combination of numbers to send and get the answer. Upon failure the server tells you the right number you missed.
My solution involved sending numbers from a text file through a fifo pipe using a loop, and adding new numbers to the text file iteratively.
There are other ways to do this, but this is the solution I came up with.
