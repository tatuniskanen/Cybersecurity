# Bad memories Part 1

In the first part of this exercise you are asked to retrieve a document from a memory dump. 

I started by installing and learning to use Volatility. The analysis starts with specifyin the os profile to Volatility using the command "imageinfo". From there I looked at what processes were running before the crash, and noticed that notepad.exe was one of the active processes. Looking at the recent console entries, i also found mentions of a document called flag.txt. I figured this is the document in question, and since it's a .txt -file, it is probably written with the active notepad.
