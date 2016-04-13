obj/Release/klbroute/route/main.o obj/Release/klbroute/route/main.d: src/route/main.cpp src/route/share/include.h \
 src/route/libstatic.h src/route/libarg.h
	rm -f obj/Release/klbroute/route/main.d
	make -f make.do obj/Release/klbroute/route/main.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__  -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__  -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbroute/route/main.o src/route/main.cpp
%.h: fail
	
.PHONY:fail
fail:
	
