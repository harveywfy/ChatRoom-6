from socket import*
from time import sleep,ctime

HOST = '10.125.115.107'#���ַ�������IP
PORT = 8086#���ַ������Ķ˿�
BUFSIZ = 1024
ADDR = (HOST,PORT)

tcpCliSock = socket(AF_INET,SOCK_STREAM)
tcpCliSock.connect(ADDR)

HOST = tcpCliSock.recv(BUFSIZ)
PORT = (int)tcpCliSock.recv(BUFSIZ)

tcpCliSock.close()

raw_input('��ͣ�У���������ʲô����')

#������ķ�����
ADDR = (HOST,PORT)
tcpCliSock = socket(AF_INET,SOCK_STREAM)
tcpCliSock.connect(ADDR)

f=open('log.txt','w')

while True:
    data = raw_input('>')
    if not data:
        break
    data = '�ͻ���˵��'+data+'('+ctime()+')'
    tcpCliSock.send(data)
    print(data)
    f.write(data)
    
    data = tcpCliSock.recv(BUFSIZ)
    if not data:
        break
    data = '������˵��'+data
    print(data)
    f.write(data)

f.close
tcpCliSock.close()
