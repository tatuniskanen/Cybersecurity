# Phishcap part 1

The phishcap name is a reasonable hint for this exercise. The tool to get started here is Wireshark. 

Often phishing attempts involve a HTTP-protocol related download of some sort, so I started filtering for the malicious event from the packet capture with http.request.method == “GET", to look for data that has been transferred to the company's host via HTTP. I found the invite_to_ski_trip.docx very quickly, because there were some obvious hints in this challenge, like the hosting site of the document being http://malicious.pw/invite_to_ski_trip.docx. 

The flag was encrypted in Rot13 within the document.
