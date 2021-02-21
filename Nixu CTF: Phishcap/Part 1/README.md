# Phishcap part 1

## Assignment 
"You have received a call from a customer (ACME) saying that there has been an accident at their factory and they want you to rule out any foul play. They have provided a packet capture for you and said that there have been many spear phishing attempts as of late targeting their company."

Hint: "I may be the source of infection. Can you find and dissect me?"

## Execution

This part was about finding the malicious event in the packet capture. The phishing part is a reasonable hint for this exercise. The tool to get started here is Wireshark. 

Often phishing attempts involve a HTTP-protocol related download of some sort, so I started filtering for the malicious event from the packet capture with http.request.method == “GET", to look for data that has been transferred to the company's host via HTTP. I found the invite_to_ski_trip.docx very quickly, because there were some obvious hints in this challenge, like the hosting site of the document being http://malicious.pw/invite_to_ski_trip.docx. Also there started to be a lot of suspicious traffic after the download.

The flag was encrypted in Rot13 within the document.
