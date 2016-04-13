obj/Debug/klbroute/route/main.o obj/Debug/klbroute/route/main.d: src/route/main.cpp src/route/share/include.h \
 src/route/share/debug.h src/route/libstatic.h src/route/libarg.h
	rm -f obj/Debug/klbroute/route/main.d
	make -f make.do obj/Debug/klbroute/route/main.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__  -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__  -D__DEBUG__ -c -o obj/Debug/klbroute/route/main.o src/route/main.cpp
%.h: fail
	
.PHONY:fail
fail:
	
