#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <string>
using namespace std;

int main() { // 生成：边界数据，随机数据，特例数据
    // 暂时没有看到egi 和 emi
    
    FILE *out;
    out = fopen("in3.txt", "w");
    //out = fopen("E:\\software_data\\OO_task\\homework\\tester_of_homework9\\input\\in.txt", "w");
    //out = fopen("in.txt", "w");
    srand(time(0));

    int totalnum = 11000;
    int op, max_person_id = 1500, max_group_id = 2, max_message_id = 2;
    string str;
    int id1, id2, value, idg, idm;


    fprintf(out,"ag 0\n");

    for(int i=0; i<1300; i++) {
        str = "name_" + to_string(i);
        fprintf(out,"ap %d %s %d\n", i, str.c_str(), rand() % 201);
        fprintf(out,"atg %d %d\n", i, 0);
    }


    for(int i=0; i < 1299; i++) {
        
        fprintf(out,"ar %d %d %d\n", i, i+1, rand() % 1001);
    } 


    for(int i=1;i<=1100;i++) {
        fprintf(out,"qgvs 0\n");
    }

    fclose(out);
    return 0;
}



