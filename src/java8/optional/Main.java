package java8.optional;

import java.util.Optional;

public class Main {

    public static void main(String[] args) {
	Optional<Computer> computer = Optional.of(new Computer());
	computer.flatMap(Computer::getSoundCard).flatMap(SoundCard::getUsb).map(Usb::getVersion).orElse("UNKNOWN");
    }

}
