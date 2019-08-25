#!/usr/bin/env python
# -*- coding: utf-8 -*-
import sys
from selenium import webdriver


def main(a):
    driver = webdriver.Chrome()  # 打开浏览器
    driver.get(a)


if __name__ == "__main__":
    print len(sys.argv)
    for i in range(1, len(sys.argv)):
        url = sys.argv[i]
        print url
