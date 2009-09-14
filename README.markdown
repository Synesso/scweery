# Scweery

Ohai there! Thanks for coming to see _Scweery_. I am happy to have you here!

At this point in time Scweery is just a pup. It can only do a few simple tricks and will probably poop on your rug. But please feel free to play with it anyway. And do [let me know](http://twitter.com/scweery) twitter of any bad behaviour new tricks you'd like to see.

## But what is it?

Easy. Scweery is a teeny weeny wrapper around JDBC that could be useful to you if you want to muck around with your database in SQL.

For instance, just say you want to manipulate some data in a table as a list of objects, you could do something like this:

<pre>
using(petsDB) { connection =>
  val findHogsQuery = "select name, gender from pets where family='erinaceidae' sort by cuteness" 
  val listOfHedgehogs = connection.inferListOf[Hedgehog](findHogsQuery) { row =>
    new Hedgehog(row.string("name"), row.string("gender"))
  }
}
</pre>

Scweery will take care of opening the connection, and closing it for you when you're done.

With all of your pet hedgehogs nicely lined up in a list, you'll probably want to:

<pre>listOfHedgehogs.foreach(_.hug)</pre>

... but that's your business.


## How do I build it?

You should use [Simple Build Tool](http://code.google.com/p/simple-build-tool/) - it is the most triumphant of build tools for cool Scala projects. When you have it installed, go to the project base and create the _package_. Do you need a hint?

*sbt package*

How easy was that?


## How do I use it?

Make sure both the Scweery jar and the JDBC driver for your database are in your classpath. Then import the Scweery Connection methods into your Scala class/object (or the "REPL":http://en.wikipedia.org/wiki/REPL) with:

<pre>
import scweery._
import Scweery._
</pre>

To work with a database connection, you need to define the JDBC connection details in a happy little container called *Connection*. It needs a JDBC connection string, a username and a password. Yup, a plaintext password.

<pre>val petsDB = new Connection("jdbc:vendorx:localhost", "synesso", "e1337^hacksaw")</pre>

Nothing's happened with the database yet. It's just an object. To open a connection you first declare that you're _using_ it.

<pre>
use(petsDB) { connection =>
  // connection is open!
}
</pre>

You can also use the database to make things. You can, as long as you know what it is you want to make. That would look more like this:

<pre>
infer[FurryFriend](petsDB) { connection =>
  // connection is open and ready for you to make FurryFriends!
}
</pre>

The connection is opened at the start of the block and will be closed automatically when the block is done.

With one of these crazy mongrels in your namespace you can _use_ it or you can _make_ lists of stuff with it. Use it when you don't need to create a list of objects.

<pre>
connection.use("select facebookid from friends") { row =>
  SpamMachine sendFreeGiftTo Friend(row.int("facebookid"))
}
</pre>

In the example above, the spam machine will be asked to send virtual teddy bears, mafia hits or farmyard animals to all of your lucky friends.

If you're more of a functional type, you might want to _infer_ a list of things.

<pre>
val drinks = connection.inferListOf[Espresso]("select name, cup_of_excellence from cafes where city='melbourne'") { row =>
  val name = row.string(0)
  val cupOfExcellence = row.string(1)
  new Espresso(name, cupOfExcellence)
}
</pre>

Now you have drinks! It is a _List[Espresso]_ selected from many well-known Melbourne cafes. Mint as.