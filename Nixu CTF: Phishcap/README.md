# Phishcap

## Assignment

https://thenixuchallenge.com/c/phishcap_part1/

"You have received a call from a customer (ACME) saying that there has been an accident at their factory and they want you to rule out any foul play. They have provided a packet capture for you and said that there have been many spear phishing attempts as of late targeting their company."

This exercise is about learning to spot malicious network activity within a packet capture with wireshark, and follow up on it by dissecting and analyzing the threat. The exercise is divided into 6 parts, which I will document individually in seperate folders. Here I will document my findings and the exercise as a whole in the report below, as I would to a customer (I will expand on this as I make progress).

### Part 1

* Analyzing the packet capture with wireshark to find the threat.
* Dissecting the threat


## The report

"We have analyzed the packet capture you provided. It seems there has been a succesful phishing attempt at your company, and an employee has opened a malicious link received via email. On february 15th, at 13:53 the employee downloaded a Microsoft Word document called "invite_to_ski_trip.docx". The file was made to look like an actual company retreat invite, but was in reality holding a malware. The file was claiming to contain pictures of the trip destination, which are behind links referring to other files. The document was asking the reader to update the document with the data from the linked files, to which your emplyee agreed. This seems to have opened a reverse-shell attack.. (to be continued)"
