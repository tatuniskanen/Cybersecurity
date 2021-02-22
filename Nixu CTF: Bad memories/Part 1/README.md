# Bad memories Part 1

## Assignment
https://thenixuchallenge.com/c/bad_memories_part1/

"The lead graphical designer at ACME has noticed some kind of strange activity on her computer. Their IT support believes it is a false positive and the computer will fix itself after turning it off and on again. However, the user managed to take a memory dump just before the crash. **Could you help us recover the documentation she was working on?**"

In the first part of this exercise you are asked to retrieve a text document from a memory dump. 

## Execution

I started by installing and learning to use Volatility. 

The analysis starts with specifyin the os profile to Volatility using "imageinfo". From there I looked at what processes were running before the crash, and noticed that notepad.exe was one of the active processes. Looking at the recent console entries, i also found mentions of a document called flag.txt. I figured this is the document in question, and since it's a .txt -file, it seemed highly likely that it would be written with the active notepad.exe.

I dumped the memory of notepad.exe into a seperate file, and started going through the new dump. I searched the dump with "strings" to try and find the contents of the .txt-file. I read that notepad stores text in 16-bit little indian, so I had to use the "-e l" switch. I looked for events before and after strings involving the "notepad.exe" and "flag.txt" with grep in the dump, but couldn't find the flag that way. I eneded up using grep with the word NIXU in rot13(AVKH), because I guessed the flag would be stored in that way, and I found it.
