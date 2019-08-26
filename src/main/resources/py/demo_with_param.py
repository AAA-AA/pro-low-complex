#!/usr/bin/env python
# -*- coding: utf-8 -*-
import sys

if __name__ == "__main__":
    print len(sys.argv)
    for i in range(1, len(sys.argv)):
        url = sys.argv[i]
        print url
