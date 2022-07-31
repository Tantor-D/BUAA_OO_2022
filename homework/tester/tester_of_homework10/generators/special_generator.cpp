#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <string>
using namespace std;

int main() { // 生成：边界数据，随机数据，特例数据
    // 暂时没有看到egi 和 emi
    
    FILE *out;
    out = fopen("in1.txt", "w");
    srand(time(0));

    int totalnum = 11000;
    int op, max_person_id = 1000, max_group_id = 2, max_message_id = 2;
    string str;
    int id1, id2, value, idg, idm;

    for(int i=0; i<1000; i++) {
        str = "name_" + to_string(i);
        fprintf(out,"ap %d %s %d\n", i, str.c_str(), rand() % 201);
    }

    for(int i=0; i < 3880; i++) {
        id1 = rand() % (max_person_id + 1);
        id2 = rand() % (max_person_id + 1);
        if (id1 == id2) {
            id2 = (id1 + 1) % (max_person_id + 1);
        }
        fprintf(out,"ar %d %d %d\n", id1, id2, rand() % 1001);
    } 


    for(int i=0; i < 100; i++) {
        id1 = rand() % (max_person_id + 1);
        id2 = rand() % (max_person_id + 1);
        if (id1 == id2) {
            id2 = (id1 + 1) % (max_person_id + 1);
        }
        fprintf(out,"qci %d %d\n", id1, id2);
    }

    
    for(int i=0; i<20; i++) {
        id1 = rand() % (max_person_id + 1);
        fprintf(out,"qlc %d\n", id1);
    }

    fclose(out);
    return 0;
}
