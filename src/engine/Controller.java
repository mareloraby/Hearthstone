package engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;
import model.cards.minions.Minion;
import model.cards.spells.AOESpell;
import model.cards.spells.FieldSpell;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.Spell;
import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;
import view.GameOver;
import view.GameView;
import view.HeroSelector;
import view.Popup;
import view.Versus;
import view.Turnstart;

public class Controller implements ActionListener, GameListener {
	private static boolean started = false;
	private HeroSelector hs;
	private Hero firsthero;
	private Hero secondhero;
	private JLabel head;

	public Controller() throws FullHandException, CloneNotSupportedException {

		hs = new HeroSelector();
		head = new JLabel("Choose First Hero");
		head.setForeground(new Color(247, 227, 17));
		head.setOpaque(false);
		hs.getStart().setEnabled(false);
		hs.getTop().add(head);
		head.setFont(new Font("Serif", Font.BOLD + Font.ITALIC, 23));
		JButton hunter = new JButton("Hunter");
		JButton mage = new JButton("Mage");
		JButton paladin = new JButton("Paladin");
		JButton priest = new JButton("Priest");
		JButton warlock = new JButton("Warlcok");

		hunter.setText("Hunter");
		mage.setText("Mage");
		paladin.setText("Paladin");
		priest.setText("Priest");
		warlock.setText("Warlock");

		hunter.addActionListener(this);
		mage.addActionListener(this);
		paladin.addActionListener(this);
		priest.addActionListener(this);
		warlock.addActionListener(this);

		hs.getP().add(hunter);
		hs.getP().add(mage);
		hs.getP().add(paladin);
		hs.getP().add(priest);
		hs.getP().add(warlock);
		hs.getStart().addActionListener(this);

		hs.getP().setBackground(Color.BLUE);
		hs.revalidate();
		hs.repaint();

	}

	private Game game;
	private GameView view;
	private ArrayList<JButton> firstHand;
	private ArrayList<JButton> secondHand;
	private ArrayList<JButton> firstField;
	private ArrayList<JButton> secondField;

	public Controller(Hero h1, Hero h2) throws FullHandException, CloneNotSupportedException {
		started = true;

		this.firsthero = h1;
		this.secondhero = h2;

		game = new Game(h1, h2);
		game.setListener(this);

		view = new GameView();

		view.getFirstHeroButton().addActionListener(this);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("Images/" + firsthero.getName() + "B.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(img.getWidth() / 3, img.getHeight() / 3, Image.SCALE_SMOOTH);

		ImageIcon i1 = new ImageIcon(dimg);

		view.getFirstHeroButton().setIcon(i1);

		view.getFirstHeroDetails().setFont(new Font("SansSerif", Font.PLAIN, 13));
		updateHeroDetails(firsthero);

		try {
			img = ImageIO.read(new File("Images/" + firsthero.getName() + "Power1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		dimg = img.getScaledInstance((int) (img.getWidth() * 0.6), (int) (img.getHeight() * 0.6), Image.SCALE_SMOOTH);

		ImageIcon icon = new ImageIcon(dimg);
		view.getFirstHeroPower().setBackground(Color.GREEN);
		view.getFirstHeroPower().setIcon(icon);
		view.getFirstHeroPower().setContentAreaFilled(false);
		view.getFirstHeroPower().setBorderPainted(true);
		view.getFirstHeroPower().setOpaque(false);
		view.getFirstHeroPower().addActionListener(this);

		view.getFirstHeroUseCard().addActionListener(this);

		view.getSecondHeroButton().addActionListener(this);

		try {
			img = ImageIO.read(new File("Images/" + secondhero.getName() + "B.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		dimg = img.getScaledInstance(img.getWidth() / 3, img.getHeight() / 3, Image.SCALE_SMOOTH);

		ImageIcon i2 = new ImageIcon(dimg);

		view.getSecondHeroButton().setIcon(i2);

		view.getSecondHeroDetails().setFont(new Font("SansSerif", Font.PLAIN, 13));
		updateHeroDetails(secondhero);

		try {
			img = ImageIO.read(new File("Images/" + secondhero.getName() + "Power1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		dimg = img.getScaledInstance((int) (img.getWidth() * 0.6), (int) (img.getHeight() * 0.6), Image.SCALE_SMOOTH);

		icon = new ImageIcon(dimg);
		view.getSecondHeroPower().setBackground(Color.GREEN);
		view.getSecondHeroPower().setIcon(icon);
		view.getSecondHeroPower().setContentAreaFilled(false);
		view.getSecondHeroPower().setOpaque(false);
		view.getSecondHeroPower().setBorderPainted(true);
		view.getSecondHeroPower().addActionListener(this);

		view.getSecondHeroUseCard().addActionListener(this);

		view.getEndTurn().addActionListener(this);

		firstHand = new ArrayList<JButton>();
		secondHand = new ArrayList<JButton>();
		firstField = new ArrayList<JButton>();
		secondField = new ArrayList<JButton>();

		for (int i = 0; i < firsthero.getHand().size(); i++) {
			JButton c = new JButton();
			c.setFont(new Font("SansSerif", Font.PLAIN, 13));
			c.setText("<html><center>" + firsthero.getHand().get(i).toString().replaceAll("\n", "<br>")
					+ "</center></html>");
			c.setActionCommand("" + firsthero.getHand().get(i).hashCode());
			c.addActionListener(this);
			firstHand.add(c);

			view.getFirstH().add(c);
		}
		for (int i = 0; i < secondhero.getHand().size(); i++) {
			JButton c = new JButton();
			c.setFont(new Font("SansSerif", Font.PLAIN, 13));
			c.setText("<html><center>" + secondhero.getHand().get(i).toString().replaceAll("\n", "<br>")
					+ "</center></html>");
			c.setActionCommand("" + secondhero.getHand().get(i).hashCode());
			c.addActionListener(this);
			secondHand.add(c);
			view.getSecondH().add(c);
		}
		changeHandVisibility();
		btnText();
		turnstarter();
	}

	private static JButton selectedHandCardButton;
	private static JButton selectedFieldMinionButton;

	private static int castedSpellButtonIndex = -1;

	private static boolean powerBuffer = false;

	private static Minion selectedFieldMinion;

	private static boolean mageminionfirst;
	private static boolean mageminionsecond;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (started) {
			JButton b = (JButton) e.getSource();

			if (e.getActionCommand().equals("EndTurn")) {
				if (selectedHandCardButton != null)
					resetSelectedHandCardButton();
				if (selectedFieldMinionButton != null) {
					selectedFieldMinion = null;
					resetSelectedFieldMinionButton();
				}
				if (castedSpellButtonIndex != -1)
					castedSpellButtonIndex = -1;

				try {
					game.endTurn();
				} catch (CloneNotSupportedException e1) {
					new Popup(e1.getMessage(), 400, 220);
				} catch (FullHandException e1) {
					new Popup(
							"<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
									+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
							400, 350);
				}
				if (game.getCurrentHero().getCurrentHP() != 0) {
					updateHeroField(firsthero);
					updateHeroField(secondhero);
					resetPowers();
					changeHandVisibility();
					updateHand(game.getCurrentHero());
					turnstarter();
					updateHeroDetails(firsthero);
					updateHeroDetails(secondhero);
				}
			}

//FIRSThERO
			else if (game.getCurrentHero() == firsthero) {
				if (b.getActionCommand().equals("secondhero") && selectedFieldMinion != null && !powerBuffer
						&& castedSpellButtonIndex == -1) {
					try {
						firsthero.attackWithMinion(selectedFieldMinion, secondhero);
						updateHeroField(firsthero);
						updateHeroDetails(secondhero);
					} catch (CannotAttackException | NotYourTurnException | TauntBypassException | NotSummonedException
							| InvalidTargetException e1) {

						new Popup("" + e1.getMessage(), 400, 220);
					}
					selectedFieldMinion = null;
					resetSelectedFieldMinionButton();
				} else if (secondField.contains(b) && castedSpellButtonIndex == -1 && !powerBuffer
						&& selectedFieldMinion != null) {

					int j = secondField.indexOf(b);

					try {
						firsthero.attackWithMinion(selectedFieldMinion, secondhero.getField().get(j));
						updateHeroField(firsthero);
						updateHeroField(secondhero);
					} catch (CannotAttackException | NotYourTurnException | TauntBypassException
							| InvalidTargetException | NotSummonedException e1) {

						new Popup("" + e1.getMessage(), 400, 220);
					}
					selectedFieldMinion = null;
					resetSelectedFieldMinionButton();

				} else if (firstField.contains(b) && castedSpellButtonIndex == -1 && !powerBuffer) {
					if (b == selectedFieldMinionButton) {
						selectedFieldMinion = null;
						resetSelectedFieldMinionButton();
					} else {
						int j = firstField.indexOf(b);
						selectedFieldMinion = firsthero.getField().get(j);
						if (selectedFieldMinionButton != null)
							resetSelectedFieldMinionButton();
						if (selectedHandCardButton != null)
							resetSelectedHandCardButton();
						selectedFieldMinionButton = b;
						selectedFieldMinionButton.setBackground(Color.green);
					}
				} else if ((e.getActionCommand().equals("fPower") || powerBuffer) && castedSpellButtonIndex == -1) {

					if (selectedFieldMinionButton != null) {
						selectedFieldMinion = null;
						resetSelectedFieldMinionButton();
					}
					if (selectedHandCardButton != null)
						resetSelectedHandCardButton();

					if (e.getActionCommand().equals("fPower")) {

						if (firsthero.getCurrentManaCrystals() >= 2 && firsthero.isHeroPowerUsed() == false) {
							if (powerBuffer == false) {
								powerBuffer = true;
							}
							view.getFirstHeroPower().setContentAreaFilled(true);

						} else if (firsthero.isHeroPowerUsed() == true) {
							try {
								throw new HeroPowerAlreadyUsedException();
							} catch (HeroPowerAlreadyUsedException e1) {
								new Popup("I already used my hero power", 400, 220);

							}
						} else {
							try {
								throw new NotEnoughManaException();
							} catch (NotEnoughManaException e1) {

								new Popup("I don't have enough mana !!", 400, 220);
							}
						}
					}

					if (powerBuffer == true) {

						if (firsthero instanceof Mage) {

							view.getFirstHeroUseCard().setText("Select Target");

							if (e.getActionCommand().equals("secondhero")) {

								try {
									((Mage) firsthero).useHeroPower(secondhero);
									updateHeroDetails(secondhero);
									updateHeroDetails(firsthero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {
									e1.printStackTrace();
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}

								view.getFirstHeroPower().setContentAreaFilled(false);

								powerBuffer = false;

								btnText();

							} else if (e.getActionCommand().equals("firsthero")) {

								try {
									((Mage) firsthero).useHeroPower(firsthero);
									updateHeroDetails(firsthero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {
									e1.printStackTrace();
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}

								view.getFirstHeroPower().setContentAreaFilled(false);

								powerBuffer = false;

								btnText();

							} else if (secondField.contains(b)) {

								int i = secondField.indexOf(b);

								try {
									((Mage) firsthero).useHeroPower((Minion) (secondhero.getField().get(i)));
									updateHeroField(secondhero);
									updateHeroDetails(firsthero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {
									new Popup("" + e1.getMessage(), 400, 220);
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getFirstHeroPower().setContentAreaFilled(false);
								powerBuffer = false;

								btnText();

							}

							else if (firstField.contains(b)) {

								int i = firstField.indexOf(b);

								try {
									((Mage) firsthero).useHeroPower((Minion) (firsthero.getField().get(i)));
									updateHeroField(firsthero);
									updateHeroDetails(firsthero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {
									new Popup("" + e1.getMessage(), 400, 220);
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getFirstHeroPower().setContentAreaFilled(false);
								powerBuffer = false;

								btnText();

							}

						} else if (firsthero instanceof Hunter) {

							try {
								((Hunter) firsthero).useHeroPower();
								updateHeroDetails(secondhero);
								updateHeroDetails(firsthero);
							} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
									| FullFieldException | CloneNotSupportedException e1) {
								new Popup("" + e1.getMessage(), 400, 220);
							} catch (FullHandException e1) {
								new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
										+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>", 400,
										350);
							}
							view.getFirstHeroPower().setContentAreaFilled(false);
							powerBuffer = false;

						} else if (firsthero instanceof Priest) {

							view.getFirstHeroUseCard().setText("Select To Heal");

							if (e.getActionCommand().equals("firsthero")) {
								try {
									((Priest) firsthero).useHeroPower(firsthero);
									updateHeroDetails(firsthero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {
									new Popup("" + e1.getMessage(), 400, 220);
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getFirstHeroPower().setContentAreaFilled(false);
								btnText();
								powerBuffer = false;

							} else if (e.getActionCommand().equals("secondhero")) {

								try {
									((Priest) firsthero).useHeroPower(secondhero);
									updateHeroDetails(secondhero);
									updateHeroDetails(firsthero);

								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {
									new Popup(e1.getMessage(), 400, 220);
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getFirstHeroPower().setContentAreaFilled(false);
								btnText();

								powerBuffer = false;

							} else if (firstField.contains(b)) {

								int i = firstField.indexOf(b);

								try {
									((Priest) firsthero).useHeroPower((Minion) (firsthero.getField().get(i)));
									updateHeroField(firsthero);
									updateHeroDetails(firsthero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {
									new Popup(e1.getMessage(), 400, 220);

								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getFirstHeroPower().setContentAreaFilled(false);
								btnText();

								powerBuffer = false;

							} else if (secondField.contains(b)) {

								int i = secondField.indexOf(b);

								try {
									((Priest) firsthero).useHeroPower((Minion) (secondhero.getField().get(i)));
									updateHeroField(secondhero);
									updateHeroDetails(firsthero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {
									new Popup(e1.getMessage(), 400, 220);

								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getFirstHeroPower().setContentAreaFilled(false);
								btnText();

								powerBuffer = false;

							}

						} else if (firsthero instanceof Warlock) {
							try {

								((Warlock) firsthero).useHeroPower();
								updateHand(firsthero);

							} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
									| FullFieldException | CloneNotSupportedException e1) {

								new Popup(e1.getMessage(), 400, 220);
							} catch (FullHandException e1) {
								new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
										+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>", 400,
										350);
							}
							updateHeroDetails(firsthero);
							view.getFirstHeroPower().setContentAreaFilled(false);
							powerBuffer = false;

						} else if (firsthero instanceof Paladin) {

							try {

								((Paladin) firsthero).useHeroPower();

								JButton silver = new JButton();

								silver.setFont(new Font("SansSerif", Font.PLAIN, 13));
								silver.setHorizontalAlignment(SwingConstants.CENTER);
								silver.setText(
										"<html><center>" + firsthero.getField().get(firsthero.getField().size() - 1)
												.toString().replaceAll("\n", "<br>") + "sleeping" + "</center></html>");
								silver.setActionCommand(
										"" + firsthero.getField().get(firsthero.getField().size() - 1).hashCode());
								silver.addActionListener(this);

								addToField(firsthero, silver);

								updateHeroDetails(firsthero);

							} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
									| FullFieldException | CloneNotSupportedException e1) {
								new Popup(e1.getMessage(), 400, 220);
							} catch (FullHandException e1) {
								new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
										+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>", 400,
										350);
							}
							view.getFirstHeroPower().setContentAreaFilled(false);
							powerBuffer = false;

						}

					}

				} else if (castedSpellButtonIndex != -1) {
					Spell castedSpell = (Spell) firsthero.getHand().get(castedSpellButtonIndex);
					if (e.getActionCommand().equals("secondhero") && castedSpell instanceof HeroTargetSpell) {
						try {
							firsthero.castSpell((HeroTargetSpell) castedSpell, secondhero);
							removeFromHand(firsthero, castedSpellButtonIndex);
							updateHand(firsthero);
							updateHeroDetails(secondhero);
							updateHeroDetails(firsthero);
						} catch (NotYourTurnException | NotEnoughManaException e1) {
							new Popup(e1.getMessage(), 400, 220);
						}
					} else if (e.getActionCommand().equals("firsthero") && castedSpell instanceof HeroTargetSpell) {
						try {
							firsthero.castSpell((HeroTargetSpell) castedSpell, firsthero);
							removeFromHand(firsthero, castedSpellButtonIndex);
							updateHand(firsthero);
							updateHeroDetails(firsthero);
						} catch (NotYourTurnException | NotEnoughManaException e1) {
							new Popup(e1.getMessage(), 400, 220);
						}
					} else if (secondField.contains(b)) {
						int i = secondField.indexOf(b);
						if (castedSpell instanceof MinionTargetSpell) {
							try {
								firsthero.castSpell((MinionTargetSpell) castedSpell,
										(Minion) (secondhero.getField().get(i)));
								removeFromHand(firsthero, castedSpellButtonIndex);
								updateHand(firsthero);
								updateHeroField(secondhero);
								updateHeroDetails(firsthero);

							} catch (NotYourTurnException | NotEnoughManaException | InvalidTargetException e1) {
								new Popup(e1.getMessage(), 400, 220);
							}

						} else {
							try {
								firsthero.castSpell((LeechingSpell) castedSpell,
										(Minion) (secondhero.getField().get(i)));
								removeFromHand(firsthero, castedSpellButtonIndex);
								updateHeroField(secondhero);
								updateHand(firsthero);
								updateHeroDetails(firsthero);

							} catch (NotYourTurnException | NotEnoughManaException e1) {
								new Popup(e1.getMessage(), 400, 220);
							}
						}
					} else if (firstField.contains(b)) {
						int i = firstField.indexOf(b);
						if (castedSpell instanceof MinionTargetSpell) {
							try {
								firsthero.castSpell((MinionTargetSpell) castedSpell,
										(Minion) (firsthero.getField().get(i)));
								removeFromHand(firsthero, castedSpellButtonIndex);
								updateHeroField(firsthero);
								updateHand(firsthero);
								updateHeroDetails(firsthero);
							} catch (NotYourTurnException | NotEnoughManaException | InvalidTargetException e1) {
								new Popup(e1.getMessage(), 400, 220);
							}

						} else {
							try {
								firsthero.castSpell((LeechingSpell) castedSpell,
										(Minion) (firsthero.getField().get(i)));
								removeFromHand(firsthero, castedSpellButtonIndex);
								updateHeroField(firsthero);
								updateHand(firsthero);
								updateHeroDetails(firsthero);
							} catch (NotYourTurnException | NotEnoughManaException e1) {
								new Popup(e1.getMessage(), 400, 220);
							}
						}
					}
					castedSpellButtonIndex = -1;
					resetSelectedHandCardButton();
// Selecting From Hand
				} else if (firstHand.contains(b)) {
					if (selectedHandCardButton == b) {
						resetSelectedHandCardButton();
					} else {
						if (selectedFieldMinionButton != null) {
							resetSelectedFieldMinionButton();
							selectedFieldMinion = null;
						}
						if (selectedHandCardButton != null)
							selectedHandCardButton.setBackground(new JButton().getBackground());
						selectedHandCardButton = b;
						int i = firstHand.indexOf(selectedHandCardButton);
						if (firsthero.getHand().get(i) instanceof Minion) {
							selectedHandCardButton.setBackground(Color.GREEN);
						} else {
							selectedHandCardButton.setBackground(new Color(153,50,204));
						}
						view.getFirstHeroUseCard().setEnabled(true);
						if (firsthero.getHand().get(firstHand.indexOf(b)) instanceof Minion) {
							view.getFirstHeroUseCard().setText("Summon Minion");
						} else {
							view.getFirstHeroUseCard().setText("Cast Spell");
						}
					}
				}

				else if (e.getActionCommand().equals("fusecard") && selectedHandCardButton != null) {
					int i = firstHand.indexOf(selectedHandCardButton);
//Minions From Hand
					if (firsthero.getHand().get(i) instanceof Minion) {
						resetSelectedHandCardButton();
						try {
							firsthero.playMinion((Minion) (firsthero.getHand().get(i)));
							addToField(firsthero, removeFromHand(firsthero, i));
							if (firsthero instanceof Mage && firsthero.fieldContains("Kalycgos")) {
								updateHandSpecialadd(firsthero);
								mageminionfirst = true;
							}
							updateHeroDetails(firsthero);
						} catch (NotYourTurnException | NotEnoughManaException | FullFieldException e1) {
							new Popup(e1.getMessage(), 400, 220);
						}
//Spells From Hand
					} else if (firsthero.getHand().get(i) instanceof AOESpell) {
						resetSelectedHandCardButton();
						try {
							firsthero.castSpell((AOESpell) firsthero.getHand().get(i), secondhero.getField());
							removeFromHand(firsthero, i);
							updateHeroField(firsthero);
							updateHeroField(secondhero);
							updateHand(firsthero);
							updateHeroDetails(firsthero);
						} catch (NotYourTurnException | NotEnoughManaException e1) {
							new Popup(e1.getMessage(), 400, 220);
						}
					} else if (firsthero.getHand().get(i) instanceof FieldSpell) {
						resetSelectedHandCardButton();
						try {
							firsthero.castSpell((FieldSpell) firsthero.getHand().get(i));
							removeFromHand(firsthero, i);
							updateHeroField(firsthero);
							updateHeroField(secondhero);
							updateHand(firsthero);
							updateHeroDetails(firsthero);
						} catch (NotYourTurnException | NotEnoughManaException e1) {
							new Popup(e1.getMessage(), 400, 220);
						}
					} else {
						if (!mageminionfirst && ((Spell) firsthero.getHand().get(i)).getManaCost() > game
								.getCurrentHero().getCurrentManaCrystals()) {
							new Popup("I don't have enough mana !!", 400, 220);
							resetSelectedHandCardButton();
						} else if (mageminionfirst && ((Spell) firsthero.getHand().get(i)).getManaCost() - 4 > game
								.getCurrentHero().getCurrentManaCrystals()) {
							new Popup("I don't have enough mana !!", 400, 220);
							resetSelectedHandCardButton();
						} else if (!(((Spell) firsthero.getHand().get(i)) instanceof HeroTargetSpell)
								&& firstField.size() == 0 && secondField.size() == 0) {
							new Popup("Field is Empty!", 400, 220);
							resetSelectedHandCardButton();
						} else {
							view.getFirstHeroUseCard().setText("Select target");
							view.getFirstHeroUseCard().setEnabled(false);
							castedSpellButtonIndex = i;
						}
					}
				}

// SECONDhERO
			} else {
				if (b.getActionCommand().equals("firsthero") && selectedFieldMinion != null && !powerBuffer
						&& castedSpellButtonIndex == -1) {
					try {
						secondhero.attackWithMinion(selectedFieldMinion, firsthero);
						updateHeroField(secondhero);
						updateHeroDetails(firsthero);
					} catch (CannotAttackException | NotYourTurnException | TauntBypassException | NotSummonedException
							| InvalidTargetException e1) {

						new Popup(e1.getMessage(), 400, 220);
					}
					selectedFieldMinion = null;
					resetSelectedFieldMinionButton();
				} else if (firstField.contains(b) && castedSpellButtonIndex == -1 && !powerBuffer
						&& selectedFieldMinion != null) {

					int j = firstField.indexOf(b);

					try {
						secondhero.attackWithMinion(selectedFieldMinion, firsthero.getField().get(j));
						updateHeroField(firsthero);
						updateHeroField(secondhero);
					} catch (CannotAttackException | NotYourTurnException | TauntBypassException
							| InvalidTargetException | NotSummonedException e1) {

						new Popup(e1.getMessage(), 400, 220);
					}
					selectedFieldMinion = null;
					resetSelectedFieldMinionButton();

				} else if (secondField.contains(b) && castedSpellButtonIndex == -1 && !powerBuffer) {
					if (b == selectedFieldMinionButton) {
						selectedFieldMinion = null;
						resetSelectedFieldMinionButton();
					} else {
						int j = secondField.indexOf(b);
						selectedFieldMinion = secondhero.getField().get(j);
						if (selectedFieldMinionButton != null)
							resetSelectedFieldMinionButton();
						if (selectedHandCardButton != null)
							resetSelectedHandCardButton();
						selectedFieldMinionButton = b;
						selectedFieldMinionButton.setBackground(Color.green);
					}
				} else if ((e.getActionCommand().equals("sPower") || powerBuffer) && castedSpellButtonIndex == -1) {
					if (e.getActionCommand().equals("sPower")) {
						if (selectedFieldMinionButton != null) {
							selectedFieldMinion = null;
							resetSelectedFieldMinionButton();
						}
						if (selectedHandCardButton != null)
							resetSelectedHandCardButton();

						if (secondhero.getCurrentManaCrystals() >= 2 && secondhero.isHeroPowerUsed() == false) {
							if (powerBuffer == false) {
								powerBuffer = true;
							}
							view.getSecondHeroPower().setContentAreaFilled(true);
						} else if (secondhero.isHeroPowerUsed() == true) {
							try {
								throw new HeroPowerAlreadyUsedException();
							} catch (HeroPowerAlreadyUsedException e1) {
								new Popup(" I already used my hero power", 400, 220);

							}
						} else {
							try {
								throw new NotEnoughManaException();
							} catch (NotEnoughManaException e1) {
								new Popup("I don't have enough mana !!", 400, 220);
							}
						}
					}

					if (powerBuffer == true) {

						if (secondhero instanceof Mage) {

							view.getSecondHeroUseCard().setText("Select Target");

							if (e.getActionCommand().equals("firsthero")) {

								try {
									((Mage) secondhero).useHeroPower(firsthero);
									updateHeroDetails(secondhero);
									updateHeroDetails(firsthero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {

									new Popup(e1.getMessage(), 400, 220);
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getSecondHeroPower().setContentAreaFilled(false);
								powerBuffer = false;

								btnText();

							} else if (e.getActionCommand().equals("secondhero")) {

								try {
									((Mage) secondhero).useHeroPower(secondhero);
									updateHeroDetails(secondhero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {

									new Popup(e1.getMessage(), 400, 220);
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getSecondHeroPower().setContentAreaFilled(false);
								powerBuffer = false;

								btnText();

							} else if (firstField.contains(b)) {

								int i = firstField.indexOf(b);

								try {
									((Mage) secondhero).useHeroPower((Minion) (firsthero.getField().get(i)));
									updateHeroField(firsthero);
									updateHeroDetails(secondhero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {

									e1.printStackTrace();
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getSecondHeroPower().setContentAreaFilled(false);
								powerBuffer = false;
								btnText();
							} else if (secondField.contains(b)) {

								int i = secondField.indexOf(b);

								try {
									((Mage) secondhero).useHeroPower((Minion) (secondhero.getField().get(i)));
									updateHeroField(secondhero);
									updateHeroDetails(secondhero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {
									new Popup(e1.getMessage(), 400, 220);
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getSecondHeroPower().setContentAreaFilled(false);
								powerBuffer = false;
								btnText();
							}

						} else if (secondhero instanceof Hunter) {

							try {
								((Hunter) secondhero).useHeroPower();
								updateHeroDetails(secondhero);
								updateHeroDetails(firsthero);
							} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
									| FullFieldException | CloneNotSupportedException e1) {
								new Popup(e1.getMessage(), 400, 220);
							} catch (FullHandException e1) {
								new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
										+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>", 400,
										350);
							}
							view.getSecondHeroPower().setContentAreaFilled(false);
							powerBuffer = false;

						} else if (secondhero instanceof Priest) {

							view.getSecondHeroUseCard().setText("Select To Heal");

							if (e.getActionCommand().equals("secondhero")) {
								try {
									((Priest) secondhero).useHeroPower(secondhero);
									updateHeroDetails(secondhero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {
									new Popup(e1.getMessage(), 400, 220);
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getSecondHeroPower().setContentAreaFilled(false);
								btnText();
								powerBuffer = false;

							} else if (e.getActionCommand().equals("firsthero")) {

								try {
									((Priest) secondhero).useHeroPower(firsthero);
									updateHeroDetails(firsthero);
									updateHeroDetails(secondhero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {
									new Popup(e1.getMessage(), 400, 220);
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getSecondHeroPower().setContentAreaFilled(false);
								btnText();
								powerBuffer = false;

							} else if (secondField.contains(b)) {

								int i = secondField.indexOf(b);

								try {
									((Priest) secondhero).useHeroPower((Minion) (secondhero.getField().get(i)));
									updateHeroDetails(secondhero);
									updateHeroField(secondhero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {
									new Popup(e1.getMessage(), 400, 220);
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getSecondHeroPower().setContentAreaFilled(false);
								btnText();
								powerBuffer = false;

							} else if (firstField.contains(b)) {

								int i = firstField.indexOf(b);

								try {
									((Priest) secondhero).useHeroPower((Minion) (firsthero.getField().get(i)));
									updateHeroDetails(secondhero);
									updateHeroField(firsthero);
								} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
										| FullFieldException | CloneNotSupportedException e1) {
									new Popup(e1.getMessage(), 400, 220);
								} catch (FullHandException e1) {
									new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
											+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>",
											400, 350);
								}
								view.getSecondHeroPower().setContentAreaFilled(false);
								btnText();
								powerBuffer = false;
							}

						} else if (secondhero instanceof Warlock) {
							try {

								((Warlock) secondhero).useHeroPower();
								updateHand(secondhero);
								updateHeroDetails(secondhero);
							} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
									| FullFieldException | CloneNotSupportedException e1) {
								new Popup(e1.getMessage(), 400, 220);
							} catch (FullHandException e1) {
								new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
										+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>", 400,
										350);
							}
							updateHeroDetails(secondhero);
							view.getSecondHeroPower().setContentAreaFilled(false);
							powerBuffer = false;

						} else if (secondhero instanceof Paladin) {

							try {

								((Paladin) secondhero).useHeroPower();

								JButton silver = new JButton();

								silver.setFont(new Font("SansSerif", Font.PLAIN, 13));
								silver.setHorizontalAlignment(SwingConstants.CENTER);
								silver.setText(
										"<html><center>" + secondhero.getField().get(secondhero.getField().size() - 1)
												.toString().replaceAll("\n", "<br>") + "</center></html>");
								silver.setActionCommand(
										"" + secondhero.getField().get(secondhero.getField().size() - 1).hashCode());
								silver.addActionListener(this);

								addToField(secondhero, silver);

								updateHeroDetails(secondhero);
							} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
									| FullFieldException | CloneNotSupportedException e1) {
								new Popup(e1.getMessage(), 400, 220);
							} catch (FullHandException e1) {
								new Popup("<html><center>" + e1.getMessage() + "<br><br>" + "Card Burned: "
										+ e1.getBurned().toString().replaceAll("\n", "<br>") + "</center></html>", 400,
										350);
							}
							view.getSecondHeroPower().setContentAreaFilled(false);
							powerBuffer = false;

						}

					}
				}

				else if (castedSpellButtonIndex != -1) {
					Spell castedSpell = (Spell) secondhero.getHand().get(castedSpellButtonIndex);

					if (e.getActionCommand().equals("firsthero") && castedSpell instanceof HeroTargetSpell) {
						try {
							secondhero.castSpell((HeroTargetSpell) castedSpell, firsthero);
							removeFromHand(secondhero, castedSpellButtonIndex);
							updateHand(secondhero);
							updateHeroDetails(firsthero);
							updateHeroDetails(secondhero);
						} catch (NotYourTurnException | NotEnoughManaException e1) {
							new Popup(e1.getMessage(), 400, 220);
						}
					} else if (e.getActionCommand().equals("secondhero") && castedSpell instanceof HeroTargetSpell) {
						try {
							secondhero.castSpell((HeroTargetSpell) castedSpell, secondhero);
							removeFromHand(secondhero, castedSpellButtonIndex);
							updateHand(secondhero);
							updateHeroDetails(secondhero);
						} catch (NotYourTurnException | NotEnoughManaException e1) {
							new Popup(e1.getMessage(), 400, 220);
						}
					} else if (secondField.contains(b)) {
						int i = secondField.indexOf(b);
						if (castedSpell instanceof MinionTargetSpell) {
							try {
								secondhero.castSpell((MinionTargetSpell) castedSpell,
										(Minion) (secondhero.getField().get(i)));
								removeFromHand(secondhero, castedSpellButtonIndex);
								updateHand(secondhero);
								updateHeroField(firsthero);
								updateHeroField(secondhero);
								updateHeroDetails(secondhero);
							} catch (NotYourTurnException | NotEnoughManaException | InvalidTargetException e1) {
								new Popup(e1.getMessage(), 400, 220);
							}

						} else {
							try {
								secondhero.castSpell((LeechingSpell) castedSpell,
										(Minion) (secondhero.getField().get(i)));
								removeFromHand(secondhero, castedSpellButtonIndex);
								updateHeroField(secondhero);
								updateHeroField(firsthero);
								updateHand(secondhero);
								updateHeroDetails(secondhero);
							} catch (NotYourTurnException | NotEnoughManaException e1) {
								new Popup(e1.getMessage(), 400, 220);
							}
						}
					} else if (firstField.contains(b)) {
						int i = firstField.indexOf(b);
						if (castedSpell instanceof MinionTargetSpell) {
							try {
								secondhero.castSpell((MinionTargetSpell) castedSpell,
										(Minion) (firsthero.getField().get(i)));
								removeFromHand(secondhero, castedSpellButtonIndex);
								updateHeroField(firsthero);
								updateHeroField(secondhero);
								updateHand(secondhero);
								updateHeroDetails(secondhero);
							} catch (NotYourTurnException | NotEnoughManaException | InvalidTargetException e1) {
								new Popup(e1.getMessage(), 400, 220);
							}

						} else {
							try {
								secondhero.castSpell((LeechingSpell) castedSpell,
										(Minion) (firsthero.getField().get(i)));
								removeFromHand(secondhero, castedSpellButtonIndex);
								updateHeroField(secondhero);
								updateHeroField(firsthero);
								updateHand(secondhero);
								updateHeroDetails(secondhero);

							} catch (NotYourTurnException | NotEnoughManaException e1) {
								new Popup(e1.getMessage(), 400, 220);
							}
						}
					}
					castedSpellButtonIndex = -1;
					resetSelectedHandCardButton();
// Selecting From Hand
				} else if (secondHand.contains(b)) {
					if (selectedHandCardButton == b) {
						resetSelectedHandCardButton();
					} else {
						if (selectedFieldMinionButton != null) {
							selectedFieldMinion = null;
							resetSelectedFieldMinionButton();
						}
						if (selectedHandCardButton != null)
							selectedHandCardButton.setBackground(new JButton().getBackground());
						selectedHandCardButton = b;
						int i = secondHand.indexOf(selectedHandCardButton);
						if (secondhero.getHand().get(i) instanceof Minion) {
							selectedHandCardButton.setBackground(Color.GREEN);
						} else {
							selectedHandCardButton.setBackground(new Color(153,50,204));
						}
						view.getSecondHeroUseCard().setEnabled(true);
						if (secondhero.getHand().get(secondHand.indexOf(b)) instanceof Minion) {
							view.getSecondHeroUseCard().setText("Summon Minion");
						} else {
							
							view.getSecondHeroUseCard().setText("Cast Spell");
						}
					}
				}

				else if (e.getActionCommand().equals("susecard") && selectedHandCardButton != null) {
					int i = secondHand.indexOf(selectedHandCardButton);
//Minions From HAnd
					if (secondhero.getHand().get(i) instanceof Minion) {
						resetSelectedHandCardButton();
						try {
							secondhero.playMinion((Minion) (secondhero.getHand().get(i)));
							addToField(secondhero, removeFromHand(secondhero, i));
							if (secondhero instanceof Mage && secondhero.fieldContains("Kalycgos")) {
								mageminionsecond = true;
								updateHandSpecialadd(secondhero);
							}
							updateHeroDetails(secondhero);
						} catch (NotYourTurnException | NotEnoughManaException | FullFieldException e1) {
							new Popup(e1.getMessage(), 400, 220);
						}
//Spells From Hand
					} else if (secondhero.getHand().get(i) instanceof AOESpell) {
						resetSelectedHandCardButton();
						try {
							secondhero.castSpell((AOESpell) secondhero.getHand().get(i), firsthero.getField());
							removeFromHand(secondhero, i);
							updateHeroField(secondhero);
							updateHeroField(firsthero);
							updateHand(secondhero);
							updateHeroDetails(secondhero);
						} catch (NotYourTurnException | NotEnoughManaException e1) {
							new Popup(e1.getMessage(), 400, 220);
						}
					} else if (secondhero.getHand().get(i) instanceof FieldSpell) {
						resetSelectedHandCardButton();
						try {
							secondhero.castSpell((FieldSpell) secondhero.getHand().get(i));
							removeFromHand(secondhero, i);
							updateHeroField(firsthero);
							updateHeroField(secondhero);
							updateHand(secondhero);
							updateHeroDetails(secondhero);
						} catch (NotYourTurnException | NotEnoughManaException e1) {
							new Popup(e1.getMessage(), 400, 220);
						}
					} else {
						if (!mageminionsecond && ((Spell) secondhero.getHand().get(i)).getManaCost() > game
								.getCurrentHero().getCurrentManaCrystals()) {
							new Popup("I don't have enough mana !!", 400, 220);
							resetSelectedHandCardButton();
						} else if (mageminionsecond && ((Spell) secondhero.getHand().get(i)).getManaCost() - 4 > game
								.getCurrentHero().getCurrentManaCrystals()) {
							new Popup("I don't have enough mana !!", 400, 220);
							resetSelectedHandCardButton();
						} else if (!(((Spell) secondhero.getHand().get(i)) instanceof HeroTargetSpell)
								&& firstField.size() == 0 && secondField.size() == 0) {
							new Popup("Field is Empty!", 400, 220);
							resetSelectedHandCardButton();
						} else {
							view.getSecondHeroUseCard().setText("Select Target");
							view.getSecondHeroUseCard().setEnabled(false);
							castedSpellButtonIndex = i;
						}
					}
				}

			}

			view.getFirstH().repaint();
			view.getFirstH().revalidate();

		} else {
			actionPerformedInital(e);
		}
	}

	public void resetSelectedHandCardButton() {
		if (selectedHandCardButton != null) {
			selectedHandCardButton.setBackground(new JButton().getBackground());
			selectedHandCardButton = null;
		}
		btnText();
	}

	public void resetSelectedFieldMinionButton() {
		if (selectedFieldMinionButton != null) {
			selectedFieldMinionButton.setBackground(new JButton().getBackground());
			selectedFieldMinionButton = null;
		}
	}

	public void resetPowers() {
		if (game.getCurrentHero() == firsthero)
			view.getFirstHeroPower().setContentAreaFilled(false);
		else
			view.getSecondHeroPower().setContentAreaFilled(false);
		powerBuffer = false;

		btnText();

	}

	public JButton removeFromHand(Hero h, int i) {
		if (this.firsthero == h) {
			view.getFirstH().remove(firstHand.get(i));
			view.getFirstH().repaint();
			view.getFirstH().revalidate();
			return firstHand.remove(i);

		} else {
			view.getSecondH().remove(secondHand.get(i));
			view.getSecondH().repaint();
			view.getSecondH().revalidate();
			return secondHand.remove(i);
		}
	}

	public void updateHand(Hero h) {
		if (this.firsthero == h) {
			if (this.firstHand.size() != this.firsthero.getHand().size()) {
				for (int i = firstHand.size(); i < firsthero.getHand().size(); i++) {
					JButton c = new JButton();
					c.setFont(new Font("SansSerif", Font.PLAIN, 13));
					c.setHorizontalAlignment(SwingConstants.CENTER);
					c.setText("<html><center>" + firsthero.getHand().get(i).toString().replaceAll("\n", "<br>")
							+ "</center></html>");
					c.setActionCommand("" + firsthero.getHand().get(i).hashCode());
					c.addActionListener(this);
					firstHand.add(c);
					view.getFirstH().add(c);
					view.getFirstH().revalidate();
					view.getFirstH().repaint();
				}

			}
			if (firsthero instanceof Mage && firsthero.fieldContains("Kalycgos"))
				updateHandSpecialadd(firsthero);

		} else {
			if (this.secondHand.size() != this.secondhero.getHand().size()) {
				for (int i = secondHand.size(); i < secondhero.getHand().size(); i++) {
					JButton c = new JButton();
					c.setFont(new Font("SansSerif", Font.PLAIN, 13));
					c.setHorizontalAlignment(SwingConstants.CENTER);
					c.setText("<html><center>" + secondhero.getHand().get(i).toString().replaceAll("\n", "<br>")
							+ "</center></html>");
					c.setActionCommand("" + secondhero.getHand().get(i).hashCode());
					c.addActionListener(this);
					view.getSecondH().add(c);
					secondHand.add(c);
					view.getSecondH().repaint();
					view.getSecondH().revalidate();
				}

			}
			if (secondhero instanceof Mage && secondhero.fieldContains("Kalycgos"))
				updateHandSpecialadd(secondhero);

		}
	}

	public void turnstarter() {
		new Turnstart("" + (game.getCurrentHero() == firsthero ? "First Hero " : "Second Hero ") + "Turn");
	}

	public void updateHeroDetails(Hero h) {
		if (firsthero == h) {
			view.getFirstHeroDetails().setText(firstheroclass + "\n" + firsthero.getName() + "\n" + "MaxMana: "
					+ firsthero.getTotalManaCrystals() + "\n" + "CurrentMana: " + firsthero.getCurrentManaCrystals()
					+ "\n" + "Deck: " + firsthero.getDeck().size() + " card(s)" + "\nHP: " + firsthero.getCurrentHP());
			view.getFirstHeroDetails().repaint();
			view.getFirstHeroDetails().revalidate();
		} else {
			view.getSecondHeroDetails()
					.setText(secondheroclass + "\n" + secondhero.getName() + "\n" + "MaxMana: "
							+ secondhero.getTotalManaCrystals() + "\n" + "CurrentMana: "
							+ secondhero.getCurrentManaCrystals() + "\n" + "Deck: " + secondhero.getDeck().size()
							+ " card(s)" + "\nHP: " + secondhero.getCurrentHP());
			view.getSecondHeroDetails().repaint();
			view.getSecondHeroDetails().revalidate();
		}

	}

	public void addToField(Hero h, JButton b) {
		if (h == firsthero) {
			view.getFirstField().add(b);
			b.setText("<html><center>" + firsthero.getField().get(firstField.size()).toString().replaceAll("\n", "<br>")
					+ (firsthero.getField().get(firstField.size()).isSleeping() ? "sleeping"
							: (firsthero.getField().get(firstField.size()).isAttacked() ? "Can't Attack"
									: "Ready to Attack!"))
					+ "<br>" + "</center></html>");
			view.getFirstField().repaint();
			view.getFirstField().revalidate();
			firstField.add(b);
			b.repaint();
			b.revalidate();
		} else {
			view.getSecondField().add(b);
			b.setText(
					"<html><center>" + secondhero.getField().get(secondField.size()).toString().replaceAll("\n", "<br>")
							+ (secondhero.getField().get(secondField.size()).isSleeping() ? "sleeping"
									: (secondhero.getField().get(secondField.size()).isAttacked() ? "Can't Attack"
											: "Ready to Attack!"))
							+ "<br>" + "</center></html>");
			view.getSecondField().repaint();
			view.getSecondField().revalidate();
			secondField.add(b);
			b.repaint();
			b.revalidate();
		}
	}

	public void updateHeroField(Hero h) {
		if (h == firsthero) {
			if (mageminionfirst && !firsthero.fieldContains("Kalycgos")) {
				mageminionfirst = false;
				updatehandminus(firsthero);
			}
			for (int i = 0; i < firsthero.getField().size(); i++) {
				String ref = "" + firsthero.getField().get(i).hashCode();
				while (!ref.equals(firstField.get(i).getActionCommand())) {
					view.getFirstField().remove(firstField.get(i));
					firstField.remove(i);
				}

				firstField.get(i)
						.setText("<html><center>" + firsthero.getField().get(i).toString().replaceAll("\n", "<br>")
								+ (firsthero == game.getOpponent() ? "Waiting for turn"
										: (firsthero.getField().get(i).isSleeping() ? "sleeping"
												: (firsthero.getField().get(i).isAttacked() ? "Can't Attack"
														: "Ready to Attack!")))
								+ "<br>" + "</center></html>");

			}
			if (firsthero.getField().size() < firstField.size()) {
				for (int i = firstField.size() - 1; i >= firsthero.getField().size(); i--) {
					view.getFirstField().remove(firstField.get(i));
					firstField.remove(i);
				}
			}
			view.getFirstField().repaint();
			view.getFirstField().revalidate();

		} else {
			if (mageminionsecond && !secondhero.fieldContains("Kalycgos")) {
				mageminionsecond = false;
				updatehandminus(secondhero);
			}
			for (int i = 0; i < secondhero.getField().size(); i++) {
				String ref = "" + secondhero.getField().get(i).hashCode();
				while (!ref.equals(secondField.get(i).getActionCommand())) {
					view.getSecondField().remove(secondField.get(i));
					secondField.remove(i);

				}

				secondField.get(i)
						.setText("<html><center>" + secondhero.getField().get(i).toString().replaceAll("\n", "<br>")
								+ (secondhero == game.getOpponent() ? "Waiting for turn"
										: (secondhero.getField().get(i).isSleeping() ? "sleeping"
												: (secondhero.getField().get(i).isAttacked() ? "Can't Attack"
														: "Ready to Attack!")))
								+ "<br>" + "</center></html>");
			}
			if (secondhero.getField().size() < secondField.size()) {
				for (int i = secondField.size() - 1; i >= secondhero.getField().size(); i--) {
					view.getSecondField().remove(secondField.get(i));
					secondField.remove(i);
				}
			}
			view.getSecondField().repaint();
			view.getSecondField().revalidate();

		}

	}

	public void btnText() {
		if (game.getCurrentHero().equals(firsthero)) {
			view.getSecondHeroUseCard().setText("Not Your Turn");
			view.getSecondHeroUseCard().setEnabled(false);

			view.getFirstHeroUseCard().setText("Select Card");
			view.getFirstHeroUseCard().setEnabled(false);

			view.getSecondHeroPower().setEnabled(false);
			view.getFirstHeroPower().setEnabled(true);

		} else if (game.getCurrentHero().equals(secondhero)) {
			view.getSecondHeroUseCard().setText("Select Card");
			view.getSecondHeroUseCard().setEnabled(false);

			view.getFirstHeroUseCard().setText("Not Your Turn");
			view.getFirstHeroUseCard().setEnabled(false);

			view.getSecondHeroPower().setEnabled(true);
			view.getFirstHeroPower().setEnabled(false);

		}
	}

	public static void main(String[] args) throws FullHandException, CloneNotSupportedException {
		new Controller();

	}

	@Override
	public void onGameOver() {
		view.setVisible(false);
		if (firsthero.getCurrentHP() <= 0) {
			new GameOver("SecondHero Wins!");

		} else {
			new GameOver("FirstHero Wins!");
		}

	}

	static String firstheroclass = "";
	static String secondheroclass = "";

	public void actionPerformedInital(ActionEvent e) {
		if (e.getActionCommand().equals("Warlock") && firsthero == null) {
			try {
				firstheroclass = "Warlock";
				firsthero = new Warlock();
				head.setText("Choose Second Hero");
			} catch (IOException e1) {
				System.out.println("B");
			} catch (CloneNotSupportedException e1) {
				System.out.println("B");
			}
		} else if (e.getActionCommand().equals("Warlock") && firsthero != null && secondhero == null) {
			try {
				secondheroclass = "Warlock";
				secondhero = new Warlock();
				head.setText("Start the Game!");
				hs.getP().setVisible(false);
				hs.getStart().setEnabled(true);

			} catch (IOException | CloneNotSupportedException e1) {
				System.out.println("B");
			}

		} else if (e.getActionCommand().equals("Mage") && firsthero == null) {
			try {
				firstheroclass = "Mage";
				firsthero = new Mage();
				head.setText("Choose Second Hero");

			} catch (IOException | CloneNotSupportedException e1) {
				System.out.println("B");
			}
		} else if (e.getActionCommand().equals("Mage") && firsthero != null && secondhero == null) {
			try {
				secondheroclass = "Mage";
				secondhero = new Mage();
				head.setText("Start the Game!");
				hs.getP().setVisible(false);
				hs.getStart().setEnabled(true);

			} catch (IOException | CloneNotSupportedException e1) {
				System.out.println("B");
			}
		} else if (e.getActionCommand().equals("Hunter") && firsthero == null) {
			try {
				firstheroclass = "Hunter";
				firsthero = new Hunter();
				head.setText("Choose Second Hero");
			} catch (IOException | CloneNotSupportedException e1) {
				System.out.println("B");
			}
		} else if (e.getActionCommand().equals("Hunter") && firsthero != null && secondhero == null) {
			try {
				secondheroclass = "Hunter";
				secondhero = new Hunter();
				head.setText("Start the Game!");
				hs.getP().setVisible(false);
				hs.getStart().setEnabled(true);

			} catch (IOException | CloneNotSupportedException e1) {
				System.out.println("B");
			}
		} else if (e.getActionCommand().equals("Paladin") && firsthero == null) {
			try {
				firstheroclass = "Paladin";
				firsthero = new Paladin();
				head.setText("Choose Second Hero");
			} catch (IOException | CloneNotSupportedException e1) {
				System.out.println("B");
			}
		} else if (e.getActionCommand().equals("Paladin") && firsthero != null && secondhero == null) {
			try {
				secondheroclass = "Paladin";
				secondhero = new Paladin();
				head.setText("Start the Game!");
				hs.getP().setVisible(false);
				hs.getStart().setEnabled(true);

			} catch (IOException | CloneNotSupportedException e1) {
				System.out.println("B");
			}
		} else if (e.getActionCommand().equals("Priest") && firsthero == null) {
			try {
				firstheroclass = "Priest";
				firsthero = new Priest();
				head.setText("Choose Second Hero");
			} catch (IOException | CloneNotSupportedException e1) {
				System.out.println("B");
			}
		} else if (e.getActionCommand().equals("Priest") && firsthero != null && secondhero == null) {
			try {
				secondheroclass = "Priest";
				secondhero = new Priest();
				hs.getP().setVisible(false);
				head.setText("Start the Game!");
				hs.getStart().setEnabled(true);
			} catch (IOException | CloneNotSupportedException e1) {
				System.out.println("B");
			}
		} else if (e.getActionCommand().equals("Start game") && secondhero != null && firsthero != null) {

			hs.setVisible(false);
			new Versus("Images/" + firsthero.getName() + ".png", "Images/" + secondhero.getName() + ".png");

			try {
				new Controller(firsthero, secondhero);
			} catch (CloneNotSupportedException e1) {
				e1.printStackTrace();
			} catch (FullHandException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void changeHandVisibility() {

		if (game.getOpponent().equals(firsthero)) {
			for (int i = 0; i < firstHand.size(); i++) {
				firstHand.get(i).setEnabled(false);
				firstHand.get(i).setText("");

			}
			for (int i = 0; i < secondHand.size(); i++) {
				secondHand.get(i).setEnabled(true);
				secondHand.get(i).setText("<html><center>"
						+ secondhero.getHand().get(i).toString().replaceAll("\n", "<br>") + "</center></html>");
			}
		} else {
			for (int i = 0; i < firstHand.size(); i++) {
				firstHand.get(i).setEnabled(true);
				firstHand.get(i).setText("<html><center>"
						+ firsthero.getHand().get(i).toString().replaceAll("\n", "<br>") + "</center></html>");

			}
			for (int i = 0; i < secondHand.size(); i++) {
				secondHand.get(i).setEnabled(false);
				secondHand.get(i).setText("");
			}
		}
	}

	public void updateHandSpecialadd(Hero h) {
		if (h instanceof Mage) {

			if (h == firsthero) {
				for (int i = 0; i < firstHand.size(); i++) {
					if (firsthero.getHand().get(i) instanceof Spell) {
						firstHand.get(i)
								.setText("<html><center>" + firsthero.getHand().get(i).getName() + "<br>" + "ManaCost: "
										+ (firsthero.getHand().get(i).getManaCost() - 4 < 0 ? 0
												: firsthero.getHand().get(i).getManaCost() - 4)
										+ "<br>" + firsthero.getHand().get(i).getRarity() + "</center></html>");

					}

				}
				view.getFirstH().revalidate();
				view.getFirstH().repaint();

			} else {
				for (int i = 0; i < secondHand.size(); i++) {
					if (secondhero.getHand().get(i) instanceof Spell) {
						secondHand.get(i).setText(
								"<html><center>" + secondhero.getHand().get(i).getName() + "<br>" + "ManaCost: "
										+ (secondhero.getHand().get(i).getManaCost() - 4 < 0 ? 0
												: secondhero.getHand().get(i).getManaCost() - 4)
										+ "<br>" + secondhero.getHand().get(i).getRarity() + "</center></html>");

					}
				}
				view.getSecondH().revalidate();
				view.getSecondH().repaint();
			}
		}
	}

	public void updatehandminus(Hero h) {
		if (h == firsthero) {
			for (int i = 0; i < firstHand.size(); i++) {
				if (!firstHand.get(i).isEnabled())
					return;
				if (firsthero.getHand().get(i) instanceof Spell) {
					firstHand.get(i).setText("<html><center>"
							+ firsthero.getHand().get(i).toString().replaceAll("\n", "<br>") + "</center></html>");
					view.getFirstH().revalidate();
					view.getFirstH().repaint();
				}

			}

		} else {
			for (int i = 0; i < secondHand.size(); i++) {
				if (!secondHand.get(i).isEnabled())
					return;
				if (secondhero.getHand().get(i) instanceof Spell) {
					secondHand.get(i).setText("<html><center>"
							+ secondhero.getHand().get(i).toString().replaceAll("\n", "<br>") + "</center></html>");
					view.getSecondH().revalidate();
					view.getSecondH().repaint();
				}
			}

		}
	}

}