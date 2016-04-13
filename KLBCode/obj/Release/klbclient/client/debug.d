obj/Release/klbclient/client/debug.o obj/Release/klbclient/client/debug.d: src/client/debug.cpp
	rm -f obj/Release/klbclient/client/debug.d
	make -f make.do obj/Release/klbclient/client/debug.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__ -D__CLIENT__ -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbclient/client/debug.o src/client/debug.cpp
%.h: fail
	
.PHONY:fail
fail:
	
