# The first secret

This was an introductory exercise into the field of encryption. It involved the following message:

"NDc3NTc2NjYyMDY2NzI3MDY1NzI2NzIwNzY2NjIwNzc2ODY2NjcyMDZmNzI2NzZhNzI3MjYxMjA2ODY2M2EyMDQxNTY0YjQ4N2I3MjYxNzAzMDcxNzY2MTc0NWY3NjY2NWY2MTYyNjc1ZjY1NzIzNDc5Nzk2YzVmNzU3NjcxMzE2MTc0NWY2ZTYxNmM2Nzc1NzY2MTc0N2Q="


This is of course Base64. When converted into cleartext, it reveals the following message in Hexadecimal:

"47757666206672706572672076662077686667206f72676a7272612068663a2041564b487b72617030717661745f76665f6162675f65723479796c5f7576713161745f6e616c67757661747d"


When we convert this message into cleartext once again, it reveals the following Rot13-algorithm:

"Guvf frperg vf whfg orgjrra hf: AVKH{rap0qvat_vf_abg_er4yyl_uvq1at_nalguvat}"


When this is converted into cleartext once more, we get the flag.


