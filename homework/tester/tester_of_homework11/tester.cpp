#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <string>
#include <iostream>
#include <Windows.h>
using namespace std;

int random_test_time = 15000;

string str;
double start_time1, end_time1;
double start_time2, end_time2;

int main() {

    // Caster
    for (int i = 1; i <= random_test_time; i++) {
        printf("===================================================\n");
        system("generator.exe");

        str = "java -jar source_code/MyCode.jar < ./in.txt > ./out1.txt";
        start_time1 = clock();
        system(str.c_str());
        end_time1 = clock();
        printf("your code's run time : %.2lf s\n", (end_time1 - start_time1) / 1000);

        str = "java -jar zwt.jar < ./in.txt > ./out2.txt";
        start_time2 = clock();
        system(str.c_str());
        end_time2 = clock();
        printf("test code's run time : %.2lf s\n", (end_time2 - start_time2) / 1000);

        if (system("fc out1.txt out2.txt")) {
            printf("Caster发现错误\n");
            return 0;
        } else {
            printf("Caster通过第%d组随机数据\n", i);
        }
        printf("===================================================\n\n\n\n");
        Sleep(1000); // 等待1s
    }

    return 0;
}