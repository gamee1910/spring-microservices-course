package io.game.cards.service.impl;

import io.game.cards.common.constants.CardsConstants;
import io.game.cards.common.dto.CardsDto;
import io.game.cards.common.mapper.CardsMapper;
import io.game.cards.entity.Cards;
import io.game.cards.exception.CardAlreadyExistsException;
import io.game.cards.exception.ResourceNotFoundException;
import io.game.cards.repository.CardsRepository;
import io.game.cards.service.ICardsService;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CardsServiceImpl implements ICardsService {

    private final CardsRepository cardsRepository;

    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> optionalCards = cardsRepository.findByMobileNumber(mobileNumber);
        if (optionalCards.isPresent()) {
            throw new CardAlreadyExistsException("Card already registed with given mobile number: " + mobileNumber);
        }
        cardsRepository.save(createNewCards(mobileNumber));
    }

    private Cards createNewCards(String mobileNumber) {
        long randomCardNumber = 10000000000L + new Random().nextLong(10000000000L);
        return Cards.builder()
                .cardNumber(Long.toString(randomCardNumber))
                .mobileNumber(mobileNumber)
                .cardType(CardsConstants.CREDIT_CARD)
                .totalLimit(CardsConstants.NEW_CARD_LIMIT)
                .amountUsed(0)
                .availableAmount(CardsConstants.NEW_CARD_LIMIT)
                .build();
    }

    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards cards = cardsRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    @Override
    public boolean updateCard(CardsDto cardsDto) {
        Cards cards = cardsRepository
                .findByCardNumber(cardsDto.getCardNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Card", "CardNumber", cardsDto.getCardNumber()));
        CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(cards);
        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        Cards cards = cardsRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
        cardsRepository.deleteById(cards.getCardId());
        return true;
    }
}
