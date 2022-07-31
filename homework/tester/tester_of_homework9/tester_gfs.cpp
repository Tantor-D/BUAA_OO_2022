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
    /*str = "java -jar MyCode.jar < .\\input\\in" + to_string(i) + ".txt > .\\output\\out" + to_string(i) + ".txt";
    system(str.c_str());
    str = "java -jar homework9.jar < .\\input\\in" + to_string(i) + ".txt > .\\zgy_output\\out" + to_string(i) + ".txt";
    system(str.c_str());*/

    str = "java -jar MyCode.jar < .\\input\\in1.txt > .\\output\\out1.txt";
    start_time1 = clock();
    system(str.c_str());
    end_time1 = clock();

    str = "java -jar hw_9.jar < .\\input\\in1.txt > .\\zgy_output\\out1.txt";
    start_time2 = clock();
    system(str.c_str());
    end_time2 = clock();

    if (system("fc .\\output\\out1.txt .\\zgy_output\\out1.txt")) {
        printf("发现错误！！！\n");
        printf("your code's run time : %.1lf s\n", (end_time1 - start_time1) / 1000);
        printf("test code's run time : %.1lf s\n", (end_time2 - start_time2) / 1000);
        return 0;
    } else {
        printf("通过特殊数据\n");
        printf("your code's run time : %.1lf s\n", (end_time1 - start_time1) / 1000);
        printf("test code's run time : %.1lf s\n", (end_time2 - start_time2) / 1000);
    }


    // 在此进行随机数据测试
    for (int i = 1; i <= random_test_time; i++) {
        printf("===================================================\n");

        system(".\\data_generator\\generator.exe");


        str = "java -jar MyCode.jar < .\\input\\in.txt > .\\output\\out.txt";
        start_time1 = clock();
        system(str.c_str());
        end_time1 = clock();

        str = "java -jar hw_9.jar < .\\input\\in.txt > .\\zgy_output\\out.txt";
        start_time2 = clock();
        system(str.c_str());
        end_time2 = clock();


        if (system("fc .\\output\\out.txt .\\zgy_output\\out.txt")) {
            printf("发现错误！！！\n");
            printf("your code's run time : %.1lf s\n", (end_time1 - start_time1) / 1000);
            printf("test code's run time : %.1lf s\n", (end_time2 - start_time2) / 1000);
            return 0;
        } else {
            printf("通过第%d组随机数据\n", i);
            printf("your code's run time : %.1lf s\n", (end_time1 - start_time1) / 1000);
            printf("test code's run time : %.1lf s\n", (end_time2 - start_time2) / 1000);
        }

        printf("===================================================\n\n\n\n");
        Sleep(1000);
    }





    return 0;
}