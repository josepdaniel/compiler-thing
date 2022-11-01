#include <stdio.h>
#include <inttypes.h>
#include "print.h"

uint64_t entry();

int main(int argc, char** argv)
{
  uint64_t result;

  result = entry();
  print_result(result);
  putchar('\n');
  return 0;
}