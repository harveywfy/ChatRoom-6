#include <winsock2.h> 
#include <iostream>
#include <process.h>

using namespace std;

//��ָ�һ��ע�ͼ�¼����һ�������ļ����ִ���ļ��С����õ�lib�ؼ��֣����԰���������һ�����ļ���
#pragma comment(lib,"ws2_32.lib") 

#define PUBLIC_SIN_PORT 8888

//�������ݵ��̷߳���
unsigned __stdcall ThreadFun(LPVOID socket)
{
	SOCKET *clientSocketP = (SOCKET*)socket;

	char RecvBuffer[MAX_PATH];

	while (true)
	{
		memset(RecvBuffer, 0x00, sizeof(RecvBuffer));
		int Ret = recv(*clientSocketP, RecvBuffer, MAX_PATH, 0);
		if (Ret == 0 || Ret == SOCKET_ERROR)
		{
			cout << "�ͻ����˳�!" << endl;
			break;
		}
		cout << "���յ��ͻ���ϢΪ:" << RecvBuffer << endl;
	}
	return 0;
}

int main()
{
	//��ʼ��WSA������ʼ����socket error
	WORD sockVersion = MAKEWORD(2, 2);
	WSADATA wsaData;
	if (WSAStartup(sockVersion, &wsaData) != 0)
	{
		return 0;
	}

	//�����׽��� socket 
	SOCKET serverSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if (serverSocket == INVALID_SOCKET)
	{
		cout << "socket error !";
		return 0;
	}

	//��IP�Ͷ˿� bind   
	sockaddr_in sin;
	sin.sin_family = AF_INET;
	sin.sin_port = htons(PUBLIC_SIN_PORT);
	sin.sin_addr.S_un.S_addr = INADDR_ANY;
	if (bind(serverSocket, (LPSOCKADDR)&sin, sizeof(sin)) == SOCKET_ERROR)
	{
		cout << "bind error !";
	}

	//��ʼ���� listen  
	if (listen(serverSocket, 20) == SOCKET_ERROR)
	{
		cout << "listen error !";
		return 0;
	}

	cout << "��������������ʼ����������" << endl;

	while (true)
	{
		//ѭ������accept
		SOCKET clientSocket = 0;
		sockaddr_in remoteAddr;
		int nAddrlen = sizeof(remoteAddr);

		clientSocket = accept(serverSocket, (SOCKADDR *)&remoteAddr, &nAddrlen);
		if (clientSocket == INVALID_SOCKET)
		{
			cout << "accept error !";
			break;
		}
		cout << "���ܵ�һ�����ӣ�" << inet_ntoa(remoteAddr.sin_addr) << endl;

		//�½�������Ϣ���߳�
		SOCKET *clientSocketP = &clientSocket;
		HANDLE handle = (HANDLE)_beginthreadex(NULL, 0, ThreadFun, clientSocketP, 0, NULL);

		while (true)
		{
			//������Ϣ
			char SendBuffer[MAX_PATH];
			cin.getline(SendBuffer, sizeof(SendBuffer));
			int Ret = send(clientSocket, SendBuffer, (int)strlen(SendBuffer), 0);
			if (Ret == SOCKET_ERROR)
			{
				cout << "Send Info Error::" << GetLastError() << endl;
				break;
			}
		}

		closesocket(clientSocket);
	}

	//�رռ���
	closesocket(serverSocket);

	WSACleanup();
	return 0;
}