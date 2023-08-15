package com.example.best_travel;

import com.example.best_travel.domain.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@Slf4j
public class BestTravelApplication{
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final HotelRepository hotelRepository;
//    private final FlyRepository flyRepository;
//    private final TicketRepository ticketRepository;
//    private final ReservationRepository reservationRepository;
//    private final TourRepository tourRepository;
//    private final CustomerRepository customerRepository;

//    public BestTravelApplication(
//            HotelRepository hotelRepository,
//            FlyRepository flyRepository,
//            TicketRepository ticketRepository,
//            ReservationRepository reservationRepository,
//            TourRepository tourRepository,
//            CustomerRepository customerRepository) {
//        this.hotelRepository = hotelRepository;
//        this.flyRepository = flyRepository;
//        this.ticketRepository = ticketRepository;
//        this.reservationRepository = reservationRepository;
//        this.tourRepository = tourRepository;
//        this.customerRepository = customerRepository;
//    }

    public static void main(String[] args) {
        SpringApplication.run(BestTravelApplication.class, args);
    }

//    public void run(String... args) throws Exception {
//        var fly = flyRepository.findById(15L).get();
//        var hotel =  hotelRepository.findById(7L).get();
//        var ticket = ticketRepository.findById(UUID.fromString("12345678-1234-5678-2236-567812345678")).get();
//        var reservation = reservationRepository.findById(UUID.fromString("12345678-1234-5678-1234-567812345678")).get();
//        var costumer = customerRepository.findById("VIKI771012HMCRG093").get();
//        log.info(String.valueOf(fly));
//        log.info(String.valueOf(hotel));
//        log.info(String.valueOf(ticket));
//        log.info(String.valueOf(reservation));
//        log.info(String.valueOf(costumer));
        //this.flyRepository.selectBetweenPrice(BigDecimal.valueOf(10), BigDecimal.valueOf(15)).forEach(System.out::println);
//
//          var customer = customerRepository.findById("GOTW771012HMRGR087").orElseThrow();
//          log.info("Client name: "+customer.getFullName());
//
//          var fly = flyRepository.findById(11L).orElseThrow();
//         log.info("Fly name: "+fly.getOriginName()+ " - "+fly.getDestinyName());
//
//         var hotel = hotelRepository.findById(3L).orElseThrow();
//        log.info("Fly name: "+hotel.getName());
//
//        var tour = TourEntity.builder()
//                  .customer(customer)
//                  .build();
//        var ticket = TicketEntity.builder()
//                .id(UUID.randomUUID())
//                .price(fly.getPrice().multiply(BigDecimal.TEN))
//                .arrivalDate(LocalDate.now())
//                .departureDate(LocalDate.now())
//                .purchaseDate(LocalDate.now())
//                .customer(customer)
//                .tour(tour)
//                .fly(fly)
//                .build();
//
//        var reservation = ReservationEntity.builder()
//                .id(UUID.randomUUID())
//                .dateTimeReservation(LocalDateTime.now())
//                .dateEnd(LocalDate.now().plusDays(2))
//                .dateStart(LocalDate.now().plusDays(1))
//                .hotel(hotel)
//                .customer(customer)
//                .tour(tour)
//                .totalDays(1)
//                .price(hotel.getPrice().multiply(BigDecimal.TEN))
//                .build();
//
//        tour.addReservation(reservation);
//        tour.updateReservation();
//
//        tour.addTicket(ticket);
//        tour.updateTicket();
//        //var tourSaved = this.tourRepository.save(tour);
//        this.tourRepository.deleteById(this.tourRepository.findById(4L).get().getId());

//    }
}
