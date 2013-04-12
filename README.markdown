# Scweery

Ohai there! Thanks for coming to see _Scweery_. I am happy to have you here!

Scweery was a tiny plaything of mine back in 2009 when I was first getting into Scala. It was only ever just a pup. It can only do a few simple tricks and will probably poop on your rug. But please feel free to play with it anyway. And do [let me know](http://twitter.com/scweery) what you think.

## But what is it?

Scweery is a teeny weeny wrapper around JDBC that could be useful to you if you want to run SQL against databases and instantiate Scala objects from the data.

For instance, just say you want to manipulate some data in a table as a list of objects, you could do something like this:

<pre>
using(petsDB) { connection =>
  val findHogsQuery = "select name, gender from pets where family='erinaceidae' order by cuteness" 
  val listOfHedgehogs = connection.inferListOf[Hedgehog](findHogsQuery) { row =>
    new Hedgehog(row.string("name"), row.string("gender"))
  }
  listOfHedgehogs.foreach(_.hug)
}
</pre>

Scweery will take care of opening the connection, and closing it for you when you're done. Removing the hedgehog quills from your flesh is your problem. 


## How do I build it?

You should use [Simple Build Tool](http://code.google.com/p/simple-build-tool/) - it is the most triumphant of build tools for cool Scala projects. When you have it installed, go to the project base and create the _package_. Do you need a hint?

<pre>
$ sbt package
</pre>


## Is there a repository?

Rather than build it, you can just fetch it from the repo. Use the following magic in your [SBT](http://code.google.com/p/simple-build-tool/) project definition:

<pre>
  val badgerhunt = "Scweery Repo" at "http://github.com/Synesso/scweery/raw/master/repo/"
  val scweery = "net.badgerhunt" % "scweery" % "0.1.2"
</pre>


## How do I use it?

Make sure both the Scweery jar and the JDBC driver for your database are in your classpath. Then import the Scweery classes and static methods into your Scala class/object (or the [REPL](http://en.wikipedia.org/wiki/REPL)) with:

<pre>
import scweery._  // Connection
import Scweery._  // use & infer methods
</pre>

### Defining a DB connection

To work with a database connection, you need to define the JDBC connection details in a happy little container called *Connection*. It needs a JDBC connection string, a username and a password. Yup, a plaintext password.

<pre>val petsDB = new Connection("jdbc:vendorx:localhost", "synesso", "e1337^hacksaw")</pre>

### Using a connection

Nothing's happened with the database yet. It's just a bean. To open a connection you first declare that you are goin to _use_ it.

<pre>
use(petsDB) { connection =>
  // connection is open!
}
</pre>

The connection is opened at the start of the block and will be closed automatically when the block is done.

### Infering an object from a connection

You can also use the database to make things. You can, as long as you know what it is you want to make. That would look more like this:

<pre>
infer[FurryFriend](petsDB) { connection =>
  // connection is open and ready for you to make a FurryFriend!
}
</pre>

### Invoking the SQL 

With one of these crazy mongrels in your namespace you can _use_ it or you can _infer_ lists of stuff with it. Use it when you don't need to create a list of objects.

<pre>
use(friendsDB) { connection =>
  connection.use("select facebookid from friends") { row =>
    SpamMachine sendFreeGiftTo Friend(row.int("facebookid"))
  }
}
</pre>

In the example above, the spam machine will be asked to send virtual teddy bears, mafia hits or farmyard animals to all of your "lucky" friends.

### Invoking the SQL to infer a list of objects

If you're more of a functional type, you might want to _infer_ a list of things.

<pre>
infer[Tray[Espresso]](cafeDB) { connection =>
  val drinks = connection.inferListOf[Espresso]("select name, cup_of_excellence from cafes where city='melbourne'") { row =>
    val name = row.string(0)
    val cupOfExcellence = row.string(1)
    new Espresso(name, cupOfExcellence)
  }
  new Tray(drinks)
}
</pre>

Now you have drinks! It is a _Tray_ of _Espresso_ drinks selected from many well-known Melbourne cafes. Mint as.


## Halp! It doesn't do what I want (or what it should).

Please tweet it [@scweery](http://twitter.com/scweery).

![scweerybird](http://en.gravatar.com/userimage/1178078/502f138122b1d59b77bc99e3068cff5d.jpg)
