/*_beginthread��_beginthreadex�Ĺ����Ӽ���
* _beginthreadex��΢���C/C++����ʱ�⺯����
* CreateThread�ǲ���ϵͳ�ĺ�����
*/

//CRT�������Ǳ�׼��C���Ժ���

/*��ЩCRT�ĺ�����malloc(),fopen(),_open(),strtok(),ctime(),
* ��localtime()�Ⱥ�����Ҫר�ŵ��ֲ߳̾��洢�����ݿ飬������ݿ�ͨ����Ҫ�ڴ����̵߳�ʱ��ͽ�����
* ���ʹ��CreateThread��������ݿ��û�н�����Ȼ��������أ�
* ���������߳��л��ǿ���ʹ����Щ��������û�г���ʵ���Ϻ�������������ݿ��ָ��Ϊ��ʱ��
* ���Լ�����һ����Ȼ�������߳���ϵ��һ������ζ���������CreateThread�������̣߳�
* Ȼ��ʹ�������ĺ���������һ���ڴ��ڲ�֪�����д������ź����ǣ���Щ������������ɾ����
* ��CreateThread��ExitThreadҲ�޷�֪������£����Ǿͻ���Memory Leak
*/
#include <stdio.h>
#include <process.h>
#include <windows.h>

//���߳�1
unsigned __stdcall ThreadFun(PVOID pM)
{
	//printf("�߳�ID��Ϊ%4d�����߳�˵��Hello World\n", GetCurrentThreadId());
	while (true)
	{
		printf("1\n");
		Sleep(1000);
	}
}

//���߳�2
unsigned __stdcall ThreadFun2(LPVOID args)
{
	int *c = (int*)args;
	while (true)
	{
		printf("%d\n", *c);
		Sleep(1000);
	}
}

struct ArgList
{
	int a;
	int b;
};

//���߳�3
unsigned __stdcall ThreadFun3(LPVOID args)
{

	ArgList *arg = (ArgList*)args;
	while (true)
	{
		printf("%d��%d\n", arg->a, arg->b);
		Sleep(1000);
	}
}

int main()
{
	//�����̺߳������ݲ�������4������Ϊnull�����һ������Ϊnull�����Զ������߳�ID
	//HANDLE handle = (HANDLE)_beginthreadex(NULL, 0, ThreadFun, NULL, 0, NULL);

	//ͬʱ���������߳�
	//const int THREAD_NUM = 5;
	//HANDLE handle[THREAD_NUM];
	//for (int i = 0; i < THREAD_NUM; i++)
	//handle[i] = (HANDLE)_beginthreadex(NULL, 0, ThreadFun, NULL, 0, NULL);

	//���̺߳������ݲ�����Ҫʹ��ָ�루void*��
	//int a = 3;
	//int* b = &a;
	//HANDLE handle2 = (HANDLE)_beginthreadex(NULL, 0, ThreadFun2, b, 0, NULL);

	//���ݶ���������߳���Ҫʹ�ýṹ��
	ArgList *arg = new ArgList();
	arg->a = 0;
	arg->b = 10;
	HANDLE handle3 = (HANDLE)_beginthreadex(NULL, 0, ThreadFun3, (LPVOID)arg, 0, NULL);

	while (true)
	{
		printf("2\n");
		Sleep(2000);
	}

	return 0;
}
