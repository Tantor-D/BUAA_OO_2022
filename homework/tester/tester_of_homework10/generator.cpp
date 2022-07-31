#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <string>
using namespace std;

int main() { // 生成：边界数据，随机数据，特例数据
    // 暂时没有看到egi 和 emi
    
    FILE *out;
    out = fopen("in.txt", "w");
    srand(time(0));

    int MaxAskNum = 5000;
    int MaxApCount = 2500;
    int MaxQcCount = 100;
    int totalnum = MaxAskNum;
    int op, max_person_id = 1, max_group_id = 1, max_message_id = 1;
    string str;
    int id1, id2, idg, idm;

    int count_ap = 0;
    int count_qc = 0;
    int count_ag = 0;
    int count_qlc = 0;

    while (totalnum--) {
        op = rand() % 17;

        if (totalnum > MaxAskNum - 1000)    // 先加1000个人
            op = 0;

        id1 = rand() % (max_person_id * 10 / 9);
        id2 = rand() % (max_person_id * 10 / 9);
        idg = rand() % (max_group_id * 10 / 9);
        idm = rand() % (max_message_id * 10 / 9);
        switch (op) {
            //PERSON & RELATION
            case 1:
                if (rand() % 20) {// 1/20的概率触发equalPersonId异常
                    fprintf(out, "ar %d %d %d\n", id1, id2, rand() % 1001);
                } else {
                    fprintf(out, "ar %d %d %d\n", id1, id1, rand() % 1001); 
                }
                
                fprintf(out, "qbs %d\n", id1);
                fprintf(out, "ar %d %d %d\n", id1, id2, rand() % 1001);
                break;

            case 2:
                if(count_qc >= MaxQcCount) 
                    break;
                fprintf(out, "qci %d %d\n", id1, id2);
                count_qc++;
                break;

            case 0:
                if(count_ap >= MaxApCount)
                    break;
                max_person_id++;
                str = "name_" + to_string(max_person_id);
                fprintf(out, "ap %d %s %d\n", max_person_id, str.c_str(), rand() % 201 );
                count_ap++;
                break;
            

            

            case 3:
                fprintf(out, "qps\n");
                break;

            case 4:
                if(count_qlc >= 20)
                    break;
                fprintf(out, "qlc %d\n", id1);
                count_qlc++;
                break;

            case 5:
                fprintf(out, "qv %d %d\n", id1, id2);
                break;

            //GROUP
            case 6:
                if (count_ag >= 20) 
                    break;
                if (rand()%4) {
                    max_group_id++;
                    fprintf(out, "ag %d\n", max_group_id);
                } else {
                    fprintf(out, "ag %d\n", rand() % max_group_id);
                }
                count_ag++;
                break;

            case 7:
                fprintf(out, "atg %d %d\n", id1, idg);
                break;

            case 8:
                fprintf(out, "dfg %d %d\n", id1, idg);
                break;

            case 9:
                fprintf(out, "qgps %d\n", idg);
                break;

            case 10:
                fprintf(out, "qgvs %d\n", idg);
                break;

            case 11:
                fprintf(out, "qgav %d\n", idg);
                break;

            //MESSAGE
            case 12:
                int this_message_id;
                if (rand() % 4) {
                    this_message_id = ++max_message_id;
                } else {
                    this_message_id = rand() % (max_message_id + 1);
                }
                
                if (rand() % 2 == 0) {
                    fprintf(out, "am %d %d %d %d %d\n", this_message_id,
                            rand() % 1000, 0, id1, id2);
                } else {
                    fprintf(out, "am %d %d %d %d %d\n", this_message_id,
                            rand() % 1000, 1, id1, idg);
                }
                break;

            case 13:
                fprintf(out, "sm %d\n", idm);
                break;

            case 14:
                fprintf(out, "qsv %d\n", id1);
                break;

            case 15:
                fprintf(out, "qrm %d\n", id1);
                break;

            case 16:
                
                break;
        }
    }

    fclose(out);
    return 0;
}



