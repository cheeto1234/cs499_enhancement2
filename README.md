
# CS-499 Enhancement 2

Capstone enhancement for Algorithms and Data Structure

## Run Locally

Download the archived binaries to run as a Java application *(Requires Java Runtime)*
## Usage

- To log into the program, specify a username and password
    1. The password must be between 8 and 20 character, have one capital letter, number, and symbol
    2. The username must be alphanumeric
- If a file does not yet exist corresponding to the username, one will be created
- Each file follows a predefined structure recognized by the program as follows:
```JSON
{
    "UID" : "username-goes-here",
    "Docs" : {
        "0" : "<- The document IDs are numbers corresponding to their indexes",
        "1" : "This part will be ecrypted at rest and contain the document's contents"
    }
}
```
- Documents can be dynamically created, modified, and deleted using the GUI
- All user files are stored in JSON format
- The GUI can also be used to log out the current user, or delete the corresponding file
## Narrative

**What is it?** This program is a graphical text editor that encrypts data at rest and
utilizes data structures to store information that can be accessed via a unique identifier
(UID). It can create and modify an unlimited number of users. Data at rest is never stored
in a plaintext format and is thus inaccessible to anyone who does not possess the decryption
password.

**Why This?** I chose to create this artifact as an expansion of the concept of my IT-145
class. The original project featured a simple MD5 module to encrypt and decrypt passwords
to mimic a user-authentication system, however, it was not functional beyond this
capacity. I chose to go a different direction with this, creating a fully-fledged (although
still simple) program to decrypt, modify, and re-encrypt data. Additionally, all the data
accessed through this program is "owned" by whatever user created it through a simple
user authentication system. It uses the Advanced Encryption Standard (AES) to protect data
at rest and UIDs to access data correctly based on the user. It also features a GUI for
logging in and performing CRUD operations on data.

**Skills Demonstrated.** I believe that this artifact well demonstrates working with data
structures and implementing algorithms to control data. When writing this program, I modularized
its functionality into libraries for reusability. All encryption, CRUD operations, file management,
and GUI components are handled dynamically by their own modules. The information for each user
is stored in an object format with indices for efficient access and modification. Additionally,
algorithms for organizing the data in said files are utilized frequently in this program. Algorithms to fetch available indices sorts current indices and finds the next available number
to be used and an index each time a new data point is created. The program allows for algorithmic
manipulation of the document object, allowing all indices to stay in order while enabling
the user to delete data points in the middle of the object without consequence. Overall, for this
program, I took a simple concept from my IT-145 class and created a functional graphical program around the idea
of utilizing data structures and algorithms to allow for dynamic manipulation of data while
encrypting all data at rest for security.

**Why are these skills important?** The skills demonstrated in this enhancement are important
as they primarily represent creating algorithmic solutions to problems that may require repetitive
functions where efficiency is important. Additionally, encrypting data at rest is a fundamental aspect
of defense-in-depth, and is crucial for protecting data in any security context.

**Course Objectives.** I think that this project meets the requirements for this milestone
as it demonstrates the usage of data structures to organize, access, and manipulate data as
well as utilizes algorithms to accomplish repetitive tasks with relative ease. The primary
reason for implementing data structures is to make the manipulation of data and access
efficient, which I believe this program accomplishes. It implements several data structures,
mainly including the array-like format that the data points are stored in (I say array-like as
they are governed by indices). The modularization of repetitive tasks for ease of reuse represents
implementing algorithms, so I believe that this project accomplishes and meets that goal as well.
## The Process
- I first began by drawing what I wanted the GUI to look like on a post-it note.
- I then began designing the login form with Eclipse's built-in window builder for Swing.
- After getting the login window to look the way I wanted, I began writing some simple logic for the buttons.
- After this, I began the development of the encryption module, which would be the basis for the whole program.
    1. I started by deciding on an algorithm and ultimately ended up going with AES.
    2. I then implemented the required functionality as a module to be used in the program.
- After implementing the encryption, I got the idea to use a custom JSON data structure.
    1. I used another post-it not to draft how I wanted it to work.
    2. After coming up with a format that seemed logical, I began working on some CRUD modules for working with my format.
- After finishing my modules, I added unit tests for posterity.
- I then implemented the editor GUI.
- I tweaked each module to work with the GUI and then implemented them (this step was easy as I had designed each module to work smoothly with the program).
- I tested the program and then added some finishing touches (More advanced logic for the buttons mostly).
