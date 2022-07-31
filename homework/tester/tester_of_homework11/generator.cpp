#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <string>
using namespace std;

int main() { // 生成：边界数据，随机数据，特例数据
    // 暂时没有看到egi 和 emi

    FILE *out;
    out = fopen("in.txt", "w");
    int is_for_hack_data = 1;

    srand(time(0));
    int MaxAskNum = (is_for_hack_data) ?  5000 : 100000;
    int INITIAL = (is_for_hack_data) ?  0 : -10000;

    

    int totalnum = MaxAskNum;
    int op, max_person_id = 1, max_group_id = 1, max_message_id = 1, max_emoji_id = 1;
    string str;
    int id1, id2, idg, idm, ide;

    int count_ap = INITIAL;
    int count_qc = INITIAL;
    int count_ag = INITIAL;
    int count_qlc = INITIAL;
    int count_sim = INITIAL;

    while (totalnum--) {
        int age = rand() % 201;
        int money = rand() % 201;
        int value = rand() % 1001;
        id1 = rand() % (max_person_id * 10 / 9);
        id2 = rand() % (max_person_id * 10 / 9);
        idg = rand() % (max_group_id * 10 / 9);
        idm = rand() % (max_message_id * 10 / 9);
        ide = rand() % (max_emoji_id * 10 / 9);

        op = rand() % 26;
        if (totalnum > MaxAskNum - 500) { // 一定要先加人和group，让max_id加上去
            op = 0;
        }

        switch (op) {
            //PERSON & RELATION
            case 0:
                if (count_ap >= 2500) {
                    totalnum++;
                    break;
                }
                max_person_id++;
                str = "name_" + to_string(max_person_id);
                fprintf(out, "ap %d %s %d\n", max_person_id, str.c_str(), age);
                //count_ap++;
                break;
            case 1:
                fprintf(out, "ar %d %d %d\n", id1, id2, value);
                break;

            case 2:
                if (count_qc >= 50) {
                    totalnum++;
                    break;
                }

                fprintf(out, "qci %d %d\n", id1, id2);
                count_qc++;
                break;

            case 3:
                fprintf(out, "qps\n");
                break;

            case 4:
                if (count_qlc >= 20) {
                    totalnum++;
                    break;
                }

                fprintf(out, "qlc %d\n", id1);
                count_qlc++;
                break;

            case 5:
                fprintf(out, "qv %d %d\n", id1, id2);
                break;

            //GROUP
            case 6:
                if (count_ag >= 20) {
                    totalnum++;
                    break;
                }
                if (rand() % 4) {
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
                fprintf(out, "qbs %d\n", id1);
                break;

            // ^ new things
            case 17:
                if (rand() % 3) {
                    fprintf(out, "arem %d %d %d %d %d\n", id1, money, 0, id1, id2);
                } else {
                    fprintf(out, "arem %d %d %d %d %d\n", id1, money, 1, id1, idg);
                }
                break;

            case 18:
                if (rand() % 3) {
                    fprintf(out, "anm %d sssss %d %d %d\n", id1, 0, id1, id2);
                } else {
                    fprintf(out, "anm %d sssss %d %d %d\n", id1, 1, id1, idg);
                }
                break;

            case 19:
                fprintf(out, "cn %d\n", id1);
                break;

            case 20:
                if (rand() % 4)
                    ide = ++max_emoji_id;

                if (rand() % 3) {
                    fprintf(out, "aem %d %d %d %d %d\n", id1, ide, 0, id1, id2);
                } else {
                    fprintf(out, "aem %d %d %d %d %d\n", id1, ide, 1, id1, idg);
                }
                break;
            case 21:
                fprintf(out, "sei %d\n", ide);
                break;

            case 22:
                fprintf(out, "qp %d\n", ide);
                break;
            case 23:
                fprintf(out, "dce %d\n", rand() % 8); // todo 这个8是随便写的
                break;
            case 24:
                fprintf(out, "qm %d\n", id1);
                break;
            case 25:
                if (count_sim >= 20) {
                    totalnum++;
                    break;
                }
                fprintf(out, "sim %d\n", idm);
                count_sim++;
                break;

        }
    }

    fclose(out);
    return 0;
}



