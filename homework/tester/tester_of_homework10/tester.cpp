#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <string>
#include <iostream>
#include <Windows.h>

using namespace std;

int main() {

    int random_test_time = 1000;

    string str;
    double start_time1, end_time1;
    double start_time2, end_time2;
    

    /*for (int i = 1; i <= 3; i++) {
        printf("===================================================\n");

        str = "java -jar MyCode.jar < in" + to_string(i) + ".txt > out1.txt";
        start_time1 = clock();
        system(str.c_str());
        end_time1 = clock();

        str = "java -jar Rider.jar < in" + to_string(i) + ".txt > out2.txt";
        start_time2 = clock();
        system(str.c_str());
        end_time2 = clock();


        if (system("fc out1.txt out2.txt")) {
            printf("���ִ��󣡣���\n");
            printf("your code's run time : %.2lf s\n", (end_time1 - start_time1) / 1000);
            printf("test code's run time : %.2lf s\n", (end_time2 - start_time2) / 1000);
            return 0;
        } else {
            printf("ͨ����%d������\n", i);
            printf("your code's run time : %.2lf s\n", (end_time1 - start_time1) / 1000);
            printf("test code's run time : %.2lf s\n", (end_time2 - start_time2) / 1000);
        }

        printf("===================================================\n\n\n\n");
        Sleep(500);
    }*/


    // �ڴ˽���������ݲ���
    for (int i = 1; i <= random_test_time; i++) {
        printf("===================================================\n");

        //system("generator.exe");
        system("strong_data_generator.exe");

        str = "java -jar MyCode.jar < in.txt > out1.txt";
        start_time1 = clock();
        system(str.c_str());
        end_time1 = clock();

        str = "java -jar Rider.jar < in.txt > out2.txt";
        start_time2 = clock();
        system(str.c_str());
        end_time2 = clock();


        if (system("fc out1.txt out2.txt")) {
            printf("���ִ��󣡣���\n");
            printf("your code's run time : %.2lf s\n", (end_time1 - start_time1) / 1000);
            printf("test code's run time : %.2lf s\n", (end_time2 - start_time2) / 1000);
            return 0;
        } else {
            printf("ͨ����%d���������\n", i);
            printf("your code's run time : %.2lf s\n", (end_time1 - start_time1) / 1000);
            printf("test code's run time : %.2lf s\n", (end_time2 - start_time2) / 1000);
        }

        printf("===================================================\n\n\n\n");
        Sleep(500);
    }


    return 0;
}