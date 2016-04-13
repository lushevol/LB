obj/Release/klbroute/route/libstatic.o obj/Release/klbroute/route/libstatic.d: src/route/libstatic.cpp src/route/share/include.h \
 src/route/libstatic.h
	rm -f obj/Release/klbroute/route/libstatic.d
	make -f make.do obj/Release/klbroute/route/libstatic.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__  -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__  -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbroute/route/libstatic.o src/route/libstatic.cpp
%.h: fail
	
.PHONY:fail
fail:
	
