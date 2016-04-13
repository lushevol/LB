obj/Release/klbroute/route/libarg.o obj/Release/klbroute/route/libarg.d: src/route/libarg.cpp src/route/libarg.h \
 src/route/share/include.h
	rm -f obj/Release/klbroute/route/libarg.d
	make -f make.do obj/Release/klbroute/route/libarg.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__  -D__RELEASE__ -DNDEBUG'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -O3  -DTIXML_USE_STL -D__EXPORTED_HEADERS__  -D__RELEASE__ -DNDEBUG -c -o obj/Release/klbroute/route/libarg.o src/route/libarg.cpp
%.h: fail
	
.PHONY:fail
fail:
	
