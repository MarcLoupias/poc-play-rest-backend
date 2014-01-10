#!/usr/bin/perl
use strict;
use warnings;

# This is an internal script to refine data and generate SQL INSERT request before loading into db.

# Perl doc
# http://perldoc.perl.org/

# r/w files
# http://www.perlfect.com/articles/perlfile.shtml

# usage :
# perl cinemas.csv.to.sql.insert.pl fr.cinemas.2012.csv out.sql
 
my $fileIn = $ARGV[0] or die "Need to get input CSV file on the command line\n";
my $fileOut = $ARGV[1] or die "Need to get output SQL file on the command line\n";
 
my $sum = 0;
open(my $data, '<', $fileIn) or die "Could not open '$fileIn' $!\n";
open(my $out, '>', $fileOut) or die "Could not open '$fileOut' $!\n";

print "parsing $fileIn file ...";

my $sqlStart = "INSERT INTO CinemaTmp (name, screen, seat, code_dep) VALUES \n";
print $out $sqlStart;
 
while (my $lineIn = <$data>) {
    chomp $lineIn; # avoid \n on last field
    $sum++;

    my @fields = split "," , $lineIn;

    my $fName = $fields[0];
    # adding MySQL quote separator char
    $fName =~ s/\'/\\\'/g;

    my $fScreen = $fields[1];

    my $fSeat = $fields[2];

    my $fDep;
    if( length($fields[3]) == 4) {
        $fDep = substr $fields[3], 0, 1;
    } else {
        $fDep = substr $fields[3], 0, 2;
    }

    my $lineOut = "(\'$fName\',$fScreen,$fSeat,\'$fDep\'),\n";
    print $out $lineOut;
}

my $sqlEnd = ";";
print $out $sqlEnd;

close($data);
close($out);
print "$sum parsed lines : done ...\n";
