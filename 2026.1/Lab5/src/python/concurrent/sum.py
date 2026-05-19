import os
import sys
import threading

def do_sum(path):
    _sum = 0
    with open(path, 'rb',buffering=0) as f:
        byte = f.read(1)
        while byte:
            _sum += int.from_bytes(byte, byteorder='big', signed=False)
            byte = f.read(1)

        print(path + " : " + str(_sum))
            

if __name__ == "__main__":
    paths = sys.argv[1:]
    threads = []
    for path in paths:
        thread = threading.Thread(target=do_sum, args=(path,))
        threads.append(thread)
        thread.start()