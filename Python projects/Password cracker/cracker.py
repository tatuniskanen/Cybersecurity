import itertools
import string

password = raw_input("Give a password: ")

# A naive password cracker to be expanded upon
# Still lacking a lot
def guess_password(real):
    chars = string.ascii_lowercase + string.ascii_uppercase + string.digits
    attempts = 0
    for password_length in range(1, 10):
        for guess in itertools.product(chars, repeat=password_length):
            attempts += 1
            guess = ''.join(guess)
            if guess == real:
                return 'password is {}. found in {} guesses.'.format(guess, attempts)
            print(guess, attempts)

print(guess_password(password))
