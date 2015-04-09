#include <winsock2.h> 
#include <iostream>
#include <process.h>

using namespace std;

//��ָ�һ��ע�ͼ�¼����һ�������ļ����ִ���ļ��С����õ�lib�ؼ��֣����԰���������һ�����ļ���
#pragma comment(lib,"ws2_32.lib") 

#define SERVER_ADD "127.0.0.1"
#define PUBLIC_SIN_PORT 8888

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
	SOCKET clientSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if (clientSocket == INVALID_SOCKET)
	{
		printf("invalid socket !");
		return 0;
	}

	sockaddr_in serAddr;
	serAddr.sin_family = AF_INET;
	serAddr.sin_port = htons(PUBLIC_SIN_PORT);
	serAddr.sin_addr.S_un.S_addr = inet_addr(SERVER_ADD);

	if (connect(clientSocket, (sockaddr *)&serAddr, sizeof(serAddr)) == SOCKET_ERROR)
	{
		cout << "Connect Error::" << GetLastError() << endl;
		return -1;
	}
	else
	{
		cout << "���ӳɹ�!" << endl;
	}

	while (true)
	{
		//������Ϣ
		char SendBuffer[MAX_PATH];
		cin.getline(SendBuffer, sizeof(SendBuffer));
		if (send(clientSocket, SendBuffer, (int)strlen(SendBuffer), 0) == SOCKET_ERROR)
		{
			cout << "Send Info Error::" << GetLastError() << endl;
			break;
		}

		//������Ϣ
		char RecvBuffer[MAX_PATH];
		memset(RecvBuffer, 0x00, sizeof(RecvBuffer));
		int Ret = recv(clientSocket, RecvBuffer, MAX_PATH, 0);
		if (Ret == 0 || Ret == SOCKET_ERROR)
		{
			cout << "������˳�!" << endl;
			break;
		}
		cout << "���յ���������ϢΪ:" << RecvBuffer << endl;

	}
	closesocket(clientSocket);

	WSACleanup();
	return 0;
}