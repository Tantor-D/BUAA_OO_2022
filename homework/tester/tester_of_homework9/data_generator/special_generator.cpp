#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <string>
using namespace std;

int main() { // ���ɣ��߽����ݣ�������ݣ���������
    FILE *out;
    //out = fopen(".\\input\\in.txt", "w");
    out = fopen("E:\\software_data\\OO_task\\homework\\tester_of_homework9\\input\\in.txt", "w");
    //out = fopen("in.txt", "w"); ����ʹ������Ļ������ɵ���
    static int Total_Request_Num = 3000;
    srand(time(0));

    int totalnum = Total_Request_Num;
    int op, max_person_id = -1, max_group_id = -1;
    string str;
    int id1, id2, value;

    while (totalnum--) {
        op = rand() % 9;
        if (totalnum > Total_Request_Num - 10) { // һ��Ҫ�ȼ��˺�group����max_id����ȥ
            op = 0;
        } else if (totalnum > Total_Request_Num - 13) {
            op = 6;
        }
        //printf("totalnum = %d  , op = %d\n", totalnum, op);
        switch (op) {
            case 0:
                max_person_id++;
                str = "name_" + to_string(max_person_id);
                fprintf(out, "ap %d %s %d\n", max_person_id, str.c_str(), rand() % 201 );
                //printf("ap %d %s %d\n", max_person_id, str.c_str(), rand() % 201 );
                break;

            case 1:
                id1 = rand() % max_person_id;
                id2 = rand() % max_person_id;
                value = rand() % 1001;
                fprintf(out, "ar %d %d %d\n", id1, id2, value);
                break;

            case 2:
                id1 = rand() % max_person_id;
                id2 = rand() % max_person_id;
                fprintf(out, "qv %d %d\n", id1, id2);
                break;

            case 3:
                fprintf(out, "qps\n");
                break;

            case 4:
                id1 = rand() % max_person_id;
                id2 = rand() % max_person_id;
                fprintf(out, "qci %d %d\n", id1, id2);
                break;

            case 5:
                fprintf(out, "qbs\n");
                break;

            case 6:
                if (rand() % 4 || totalnum > 2988) { // 3/4���ʼ��µ�
                    max_group_id++;
                    id1 = max_group_id;
                } else {
                    id1 = rand() % max_group_id;
                }
                fprintf(out, "ag %d\n", id1);
                break;

            case 7:
                id1 = rand() % max_person_id;
                id2 = rand() % max_group_id;
                fprintf(out, "atg %d %d\n", id1, id2);
                break;

            default:
                id1 = rand() % max_person_id;
                id2 = rand() % max_group_id;
                fprintf(out, "del %d %d\n", id1, id2);
        }
    }

    fclose(out);
    return 0;
}



