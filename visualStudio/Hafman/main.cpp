#include <cstdlib>
#include <cmath>
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <cstdint>
#include <vector>

using namespace std;

int main(int argc, char** argv){
	map<uint8_t, size_t> mappa;
	size_t bytes = 0;
	// Open file and start reading
	ifstream file;
	file.open("bibbia.txt", ios::binary);
	char output;
	file >> noskipws;
	if (file.is_open()){
		while (!file.eof())
		{
			file >> output;
			++bytes;
			if (mappa.find(output) == mappa.end()){
				mappa.insert(make_pair(output,1));
			}
			else{
				mappa[output] = ++mappa[output];
			}
		}
		printf("OK");
	}
	else{
		return EXIT_FAILURE;
	}
	file.close();
	vector<uint8_t, size_t> vettore(mappa.begin(), mappa.end());
	sort(vettore.begin(), vettore.end());
	ofstream fileo;
	fileo.open("out.txt");
	if (fileo.is_open()){
		// Set probabilities
		for (vector<uint8_t, size_t>::iterator =  = mappa.begin(); iterator != mappa.end(); ++iterator){
			if (iterator->first < 32 || iterator->first >= 128)
				fileo << int(iterator->first);
			else fileo << "\"" << iterator->first << "\"";
			fileo << "\t" << iterator->second << "\n";
		}
	}
	else{
		return EXIT_FAILURE;
	}
	fileo.close();
	return EXIT_SUCCESS;
}