# Rochester VIE Treasure Hunt

This repository holds the code for the Treasure Hunt project used
in the Rochester Public Schools programming competition beginning in 2016.

## License
All code in this repository is licensed under the Apache V2 license.  Details are found in the LICENSE file.


## Contributions
Contributions to this project should conform to the `Developer Certificate
of Origin` as defined at https://developercertificate.org/.
Commits to this project need to contain the following line to indicate
the submitter accepts the DCO:
```
Signed-off-by: Your Name <your_email@domain.com>
```
By contributing in this way, you agree to the terms as follows:
```
Developer Certificate of Origin
Version 1.1

Copyright (C) 2004, 2006 The Linux Foundation and its contributors.
660 York Street, Suite 102,
San Francisco, CA 94110 USA

Everyone is permitted to copy and distribute verbatim copies of this
license document, but changing it is not allowed.


Developer's Certificate of Origin 1.1

By making a contribution to this project, I certify that:

(a) The contribution was created in whole or in part by me and I
    have the right to submit it under the open source license
    indicated in the file; or

(b) The contribution is based upon previous work that, to the best
    of my knowledge, is covered under an appropriate open source
    license and I have the right under that license to submit that
    work with modifications, whether created in whole or in part
    by me, under the same open source license (unless I am
    permitted to submit under a different license), as indicated
    in the file; or

(c) The contribution was provided directly to me by some other
    person who certified (a), (b) or (c) and I have not modified
    it.

(d) I understand and agree that this project and the contribution
    are public and that a record of the contribution (including all
    personal information I submit with it, including my sign-off) is
    maintained indefinitely and may be redistributed consistent with
    this project or the open source license(s) involved.
```

## Build

### Pre-requisties

* Java SDK 7.0+
* Apache Ant

### Build steps

To build a zip file containing the treasure hunt runtime, DrJava project, and other deliverables for students, run the following:

```bash
cd myTreasureHunt
ant
```

The resulting zip will be under `myTreasureHunt/build/build4student`.

## Run

### DrJava

It is recommended to run the project like students would. To run the treasure hunt game:

1. Unzip the archive:

   ```bash
   unzip myTreasureHunt/build/build4student/student-vX.Y.zip
   ```

2. Open [DrJava](http://www.drjava.org/).
3. Open the included DrJava project by clicking 'Project -> Open' then search and open `myTreasureHunt/build/build4student/treasure_hunt_2022/drjava/treasure_hunt_2022.drjava`
4. Compile and run the project.
