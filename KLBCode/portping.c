#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <unistd.h>
#include <sys/time.h>
#include <fcntl.h>
#include <arpa/inet.h>
#include <netdb.h>

void usage(char *prog) {
	fprintf(stderr, "error: Usage: %s [-m tcp|udp] [-t timeout_sec] [-u timeout_usec] <host> <port>\n", prog);
		exit(-1);
}

int main (int argc, char *argv[]) {

	int sockfd;
	struct sockaddr_in addr;
	struct hostent *host;
	int error = 0;
	int ret;
	socklen_t errlen;
	struct timeval timeout;
	fd_set fdrset, fdwset;
	int mode = 0;
	int c;
	char *cptr;
	long timeout_sec=0, timeout_usec=0;
	int port=0;

	if (argc < 3)  {
		usage(argv[0]);
	}
	
	while((c = getopt(argc, argv, "m:t:u:")) != -1) {
		switch(c) {
			case 'm':
                                if(!strcmp(optarg, "udp"))
					mode = 1;
				break;
			case 't':
				cptr = NULL;
				timeout_sec = strtol(optarg, &cptr, 10);
				if (cptr == optarg)
					usage(argv[0]);
				break;
			case 'u':
				cptr = NULL;
				timeout_usec = strtol(optarg, &cptr, 10);
				if (cptr == optarg)
					usage(argv[0]);
				break;
			default:
				usage(argv[0]);
				break;
		}
	}
	
	if(mode)
		sockfd = socket (AF_INET, SOCK_DGRAM, 0);
	else
		sockfd = socket (AF_INET, SOCK_STREAM, 0);

	memset(&addr, 0, sizeof(addr));

	if ((host = gethostbyname(argv[optind])) == NULL) {
		exit(-1);
	}
	
	memcpy(&addr.sin_addr, host->h_addr_list[0], host->h_length);
	addr.sin_family = host->h_addrtype; /* always AF_INET */
	if (argv[optind+1]) {
		cptr = NULL;
		port = strtol(argv[optind+1], &cptr, 10);
		if (cptr == argv[optind+1])
			usage(argv[0]);
	} else {
		usage(argv[0]);
	}
	addr.sin_port = htons(port);

	fcntl(sockfd, F_SETFL, O_NONBLOCK);
	if ((ret = connect(sockfd, (struct sockaddr *) &addr, sizeof(addr))) != 0) {
		if (errno != EINPROGRESS) {
				return (-1);
		}

		FD_ZERO(&fdrset);
		FD_SET(sockfd, &fdrset);
		fdwset = fdrset;

		timeout.tv_sec=timeout_sec + timeout_usec / 1000000;
		timeout.tv_usec=timeout_usec % 1000000;

		if ((ret = select(sockfd+1, &fdrset, &fdwset, NULL, timeout.tv_sec+timeout.tv_usec > 0 ? &timeout : NULL)) == 0) {
			/* timeout */
			close(sockfd);
			return(2);
		}
		if (FD_ISSET(sockfd, &fdrset) || FD_ISSET(sockfd, &fdwset)) {
			errlen = sizeof(error);
			if ((ret=getsockopt(sockfd, SOL_SOCKET, SO_ERROR, &error, &errlen)) != 0) {
				/* getsockopt error */
				close(sockfd);
				return(-1);
			}
			if (error != 0) {
				close(sockfd);
				return(1);
			}
		} else {
			exit(-1);
		}
	}
	/* OK, connection established */
	close(sockfd);
	return 0;
}
