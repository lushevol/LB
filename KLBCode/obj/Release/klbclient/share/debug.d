obj/Release/klbclient/share/debug.o obj/Release/klbclient/share/debug.d: src/share/debug.cpp src/share/include.h src/share/debug.h
	rm -f obj/Release/klbclient/share/debug.d
	make -f make.do obj/Release/klbclient/share/debug.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbclient/share/debug.o src/share/debug.cpp
%.h: fail
	
.PHONY:fail
fail:
	
