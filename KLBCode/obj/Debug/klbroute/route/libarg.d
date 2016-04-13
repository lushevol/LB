obj/Debug/klbroute/route/libarg.o obj/Debug/klbroute/route/libarg.d: src/route/libarg.cpp src/route/libarg.h \
 src/route/share/include.h src/route/share/debug.h
	rm -f obj/Debug/klbroute/route/libarg.d
	make -f make.do obj/Debug/klbroute/route/libarg.d -e CC:='g++' CPPFLAGS:='-Wall -fexceptions -I/usr/src/linux/include -fpermissive -g' INCLUDES:='' DEFINE:='-DTIXML_USE_STL -D__EXPORTED_HEADERS__  -D__DEBUG__'
	g++ -Wall -fexceptions -I/usr/src/linux/include -fpermissive -g  -DTIXML_USE_STL -D__EXPORTED_HEADERS__  -D__DEBUG__ -c -o obj/Debug/klbroute/route/libarg.o src/route/libarg.cpp
%.h: fail
	
.PHONY:fail
fail:
	
